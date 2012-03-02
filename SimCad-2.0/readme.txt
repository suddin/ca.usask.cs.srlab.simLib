
SimCad Clone Detector
---------------------
Version 	: 2.0
Released on	: February 2012
Reference 	: http://dx.doi.org/10.1109/WCRE.2011.12
Origin 		: Software Research Laboratory, Dept. of Computer Science, University of Saskatchewan

Disclaimer
----------
The information is provided "as is", without warranty of any kind, whether expressed or implied.
Please use ALL information, commands and configuration with care and at your OWN sole responsibility.
The Authors' will not be responsible for any damages or loss of any kind resulting from its use.

Installation Steps
------------------

1. Install java 1.5 or later (http://java.sun.com/javase/downloads/index.jsp)
	1.1 Check java installation
		$ java -version 

2. Install TXL 10.5i or later (http://www.txl.ca)
	2.1 Check TXL installation
		$ txl -V

3. Install SimCad
	3.1 Extract the archive
		$ cd PATH_CONTAINING_SimCad-2.0.zip
		$ tar -xvf SimCad-2.0.zip
	3.2 $ cd SimCad-2.0
	3.3 $ make 
	3.4 Check SimCad installation
		$ ./simCad2 -version

4. Run SimCad
	4.1 $ cd PATH_CONTAINING_SimCad-2.0/SimCad-2.0
	4.2 $ ./simCad2 [-version] [-v] -s source_path -l language
               [-g granularity] [-t clone_type]
               [-c clone_grouping] [-x source_transform]
               [-o output_path]          

		-version          = display simcad version
		-v                = verbose mode, shows the detection in progress
		language          = c | java | cs | py
		granularity       = (block | b ) | (function | f) : default = function
		clone_type        = 1 | 2 | 3 | 12 | (23 | nearmiss) | 13 | (123 | all) : default = 123
		clone_grouping    = (group | cg) | (pair | cp) : default = group
		source_transform  = (generous | g) | (greedy | G) : default = generous
		source_path       = absolute path to source folder
		output_path       = absolute path to output folder : default = {source_path}_simcad_clones
		
	4.3 example
		$ ./simCad2 -s /Users/foo/Documents/workspaces/my-project -l java
		