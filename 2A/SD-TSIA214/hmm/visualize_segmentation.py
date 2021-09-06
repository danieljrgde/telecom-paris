def visualize_segmentation(mail_filename, visualized_mail_filename, path):
    ## @parameter mail_filename : Path to the mail on wich we try the algorithm.
	## @parameter visualized_mail_filename : The path on which we write the mail with the v 	
    ## @parameter path : The sequence of 0 and 1 that the Viterbi algorithm returns.
    ## return: True if the header corresponds to the
	
    i=0
    while path[i] == 0:
        i+=1
        visu = open(visualized_mail_filename, 'w')
        if(mail_filename.endswith(".dat")):
      	    mail_filename = mail_filename[:-4] + ".txt"
        mail = open(mail_filename, 'r')
        header = mail.read(i)
        visu.write(header) 
        visu.write("\n===================== cut here\n") 
        visu.write(mail.read(path.sum()))
        visu.close() 
        mail.close() 

    return

# visualize_segmentation("dat/mail1.dat", 'path_test.txt', path)
