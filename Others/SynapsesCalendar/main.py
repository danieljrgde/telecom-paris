import os.path
import pickle
import re
import time

import jicson
import pandas as pd
import requests
from bs4 import BeautifulSoup
from google.auth.transport.requests import Request
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build



#------------------------------#
#--- USER INPUTS --------------#
#------------------------------#

# Paste your synapses calendar url
URL_MY_CALENDAR = ""

# Paste the id of the calendars you want your events added to
LECTURES_CALENDAR_ID = ""    # Google calendar_id of the calendar that should contain lectures information
OPTATIVES_CALENDAR_ID = ""   # Google calendar_id of the calendar that should contain optative events (cycle d'ingenieur, ...)

# Paste the url where you can see the catalogue of your year's lectures
URL_LECTURES_CATALOGUE = "https://synapses.telecom-paris.fr/catalogue/2021-2022/diplome/4/ING-diplome-d-ingenieur"

# Update the suffixes as you wish
kinds = {
    '(Leçon)': "",
    '(Cours de Langues)': "",
    '(Cours de Formation Humaine)': "",
    '(Travaux pratiques)': "(TP)",
    '(Travaux dirigés)': "(TD)",
    '(Contrôle de connaissance)': "(Exam)",
    '(Examen Oral et Soutenance)': "(Oral Exam)",
    '(Projet)': "(Project)",
    '(Autre)': "(Other)"
}

# Set the colors you wish for each type of class (https://blog.kodono.info/wordpress/2016/04/18/get-color_id-from-a-google-calendar-using-api-p)
colors = {
    "": "",
    "(TP)": 7,
    "(TD)": 7,
    "Project": 7,
    "Other": 7,
    "(Exam)": 4,
    "(Oral Exam)": 4
}

# Set your timezone
TIMEZONE = "Europe/Paris"


#------------------------------#
#--- SCRAPE LECTURES ----------#
#------------------------------#

# Obtains the HTML of the page with the lectures
r = requests.get(URL_LECTURES_CATALOGUE)
soup = BeautifulSoup(r.text, 'html.parser')

# Extract the lectures and save them in the lectures dictionary
lectures = {}
for lecture in soup.find('table', attrs={'id': "tableau-unitesenseignementfiche"}).find('tbody').find_all('tr'):
    libelle = lecture.find('td', attrs={'class': "libelle"})
    symbol = libelle.find('span').text
    name = libelle.find('a')['title']
    lectures[symbol] = name


#------------------------------#
#--- CLEAN EVENT INFORMATIONS -#
#------------------------------#

# Obtains the events of the synapses calendar
synapses = jicson.fromWeb(URL_MY_CALENDAR, auth="")
df_events = pd.DataFrame(synapses.get("VCALENDAR\r")[-1].get("VEVENT\r"))
df_events = df_events.rename(
    columns = {
        "DTSTART;TZID=Europe/Paris": "start",
        "DTEND;TZID=Europe/Paris": "end",
        "DTSTAMP": "stamp",
        "UID": "uid",
        "CLASS": "class",
        "CREATED": "created",
        "DESCRIPTION": "desc",
        "LAST-MODIFIED": "last_modified",
        "LOCATION": "location",
        "SEQUENCE": "sequence",
        "STATUS": "status",
        "SUMMARY": "summary",
        "TRANSP": "transp",
        "X-MICROSOFT-CDO-INTENDEDSTATUS": "microsoft_intendedstatus"
    }
)

# Convert the events to the right timezone
df_events["start"] = pd.to_datetime(df_events["start"])
df_events["start"] = df_events["start"].dt.tz_localize(TIMEZONE)
df_events["end"] = pd.to_datetime(df_events["end"])
df_events["end"] = df_events["end"].dt.tz_localize(TIMEZONE)


#------------------------------#
#--- SEPARATES EVENTS ---------#
#------------------------------#

# Separates events in two calendars
optative_events = []
lectures_events = []

for idx, row in df_events.iterrows():
    
    # Sets the event structure
    event = {
        "description": row["desc"],
        "location": row["location"],
        "start": {
            "dateTime": row["start"].isoformat(),
            "timeZone": TIMEZONE,
        },
        "end": {
            "dateTime": row["end"].isoformat(),
            "timeZone": TIMEZONE,
        }
    }

    # Events that depends on a lecture
    if any(key in row["summary"] for key in lectures.keys()):

        # Set the summary of the event
        lecture = lectures.get(row["summary"].split(" ")[0])
        kind = kinds.get(re.findall(r"\(.*?\)", row["summary"])[-1])
        event["summary"] = f"{lecture} {kind}"

        # When its not a normal lecture (exam, oral exam, project, ...), add reminders
        if kind:
            event["reminders"] = {
                "useDefault": False,
                "overrides": [
                    {"method": "popup", "minutes": 60},
                    {"method": "popup", "minutes": 60*24},
                ],
            }

        # Set the color of the event according to its kind
        if colors.get(kind):
            event["colorId"] = colors.get(kind)

        lectures_events.append(event)

    # Optative events
    else:
        event["summary"] = row["summary"]
        optative_events.append(event)


#------------------------------#
#--- ADD EVENTS TO GOOGLE -----#
#------------------------------#

class Calendar:

    SCOPES = ["https://www.googleapis.com/auth/calendar"]

    def __init__(self, calendarId):
        self.creds = None
        self.calendarId = calendarId
        if os.path.exists("./credentials/token.pickle"):
            with open("./credentials/token.pickle", "rb") as token:
                self.creds = pickle.load(token)
        if not self.creds or not self.creds.valid:
            if self.creds and self.creds.expired and self.creds.refresh_token:
                self.creds.refresh(Request())
            else:
                self.creds = InstalledAppFlow.from_client_secrets_file("./credentials/credentials.json", self.SCOPES).run_local_server(port=0)
            with open("./credentials/token.pickle", "wb") as token:
                pickle.dump(self.creds, token)
        self.service = build("calendar", "v3", credentials=self.creds)

    def create_event(self, event):
        return self.service.events().insert(calendarId=self.calendarId, body=event).execute()


lectures_calendar = Calendar(LECTURES_CALENDAR_ID)
for event in lectures_events:
    print(event["summary"])
    lectures_calendar.create_event(event)
    time.sleep(0.05)

optative_calendar = Calendar(OPTATIVES_CALENDAR_ID)
for event in optative_events:
    print(event["summary"])
    optative_events.create_event(event)
    time.sleep(0.05)
