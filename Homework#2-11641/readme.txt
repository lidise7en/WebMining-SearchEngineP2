Thank you for using my codes.

The entry points are main functions in Class BM25_ranking and Class Indri to run BM25 and Indri respectively.

In Class BM25_ranking
* I have designed many inputs and outputs formats.
* The codes commented in the first four lines are for separate query. The ouput is in the file SUM_result.txt
* The second format is for csv sample. The output is in the Experiment_result.txt  
* The third format is for BOW-F.txt and BOW-S.txt. You can only comment the line parse_Tree.Write_Sample(q_id, exp_file); and uncomment the line parse_Tree.Write_Result(q_id,exp_file);
* Important: The file Experiment_result.txt is additional written, so you have to delete it every time you run the program.
* Important: The index files should be put in the resource/  . 

In Indri ranking

* I have designed many inputs and outputs formats.
* The codes commented in the first four lines are for separate query. The ouput is in the file finalIndri.txt
* The second format is for csv sample. The output is in the Experiment_result.txt  
* The third format is for BOW-F.txt and BOW-S.txt STR-SDM.txt and STR-HW1.txt. 
* You can comment the line parse_Tree.Write_Sample(q_id, exp_file);
* and uncomment the line parse_Tree.Write_Result(q_id,exp_file);
* For these four experiement sets, you should also be careful about the initial query. I have designed to formats which will add the default #AND and the other is not. Use them properly. 
* Important: The file Experiment_result.txt is additional written, so you have to delete it every time you run the program.
* I am sure that my program can run normally, so if there is an error please contact me. 
* Important: The index files should be put in the resource/  . 
Thanks for reading. 
