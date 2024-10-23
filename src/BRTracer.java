import java.io.File;

import property.Property;

public class BRTracer {
	public static void main(String[] args) {
        /* You need to manually specify three program arguments in Eclipse or Intellij before running */

		args = new String[8];
		args[0]="-d";
		args[1]="/Users/armin/Desktop/UCI/bug-localization-project/Codes/Adjusted-BRTracer/Dataset/";
		args[2]="-p";
		args[3]="eclipse";
		args[4]="-o";
		args[5]="/Users/armin/Desktop/UCI/bug-localization-project/Codes/Adjusted-BRTracer/Result";
		args[6]="-s";
		args[7]="/Users/armin/Desktop/platform/eclipse.platform.ui";

		try {
			if (args.length == 0) {
				showHelp();
			} else {
				boolean isLegal = parseArgs(args);
				if (isLegal) {
					Core core = new Core();
					core.process();
				}
			}
		} catch (Exception ex) {
			showHelp();
		}
	}

	private static void showHelp() {
		String usage = "Usage:java -jar BRTracer [-options] \r\n\r\nwhere options must include:\r\n"
				+ "-d	indicates the path of the dataset\r\n"
				+ "-p	indicates which project to evaluate (available option: jdt, swt, eclipse, aspectj)\r\n"
				+ "-o	indicates where to store the output file (require an absolute path name).\r\n"
				+ "-s	indicates the path of the source code.\r\n";

		System.out.println(usage);
	}

	private static boolean parseArgs(String[] args) {
		int i = 0;
		String bugFilePath = "";
		String sourceCodeDir = "";
        String datasetPath = "";
		String projectStr = "";
		float alpha = 0.3f;
		String outputFile = "";
		while (i < args.length - 1) {
			if (args[i].equals("-d")) {
				i++;
				datasetPath = args[i];
			} else if (args[i].equals("-p")) {
				i++;
				projectStr = args[i];
			} else if (args[i].equals("-o")) {
				i++;
				outputFile = args[i];
			}
			else if (args[i].equals("-s")){
				i++;
				sourceCodeDir = args[i];
			}
			i++;
		}
        if(projectStr.compareTo("eclipse")==0){
            bugFilePath = datasetPath+"Eclipse_Platform_UI.xml";
        }
        else if(projectStr.compareTo("swt")==0){
            bugFilePath = datasetPath+"SWT.xml";
        }
		else if(projectStr.compareTo("jdt")==0){
			bugFilePath = datasetPath+"JDT.xml";
		}
        else{
            bugFilePath = datasetPath+"AspectJ.xml";
        }
		boolean isLegal = true;
		if (datasetPath.equals("") || datasetPath == null) {
			isLegal = false;
			System.out.println("you must indicate the path of your dataset");
		}
		if (sourceCodeDir.equals("") || sourceCodeDir == null) {
			isLegal = false;
			System.out.println("you must indicate the path of your source code");
		}
		if (projectStr.equals("") || projectStr == null) {
			isLegal = false;
			System.out.println("you must indicate which project to evaluate");
		}
		if (outputFile.equals("") || outputFile == null) {
			isLegal = false;
			System.out.println("you must indicate where to store the output file");
		} else {
			File file = new File(outputFile);
			if (file.isDirectory()) {
				outputFile += "output.txt";
			}
		}
		if (!isLegal) {
			showHelp();
		} else {
			File file = new File(System.getProperty("user.dir"));
			if (file.getFreeSpace() / 1024 / 1024 / 1024 < 2) {
				System.out
						.println("Not enough free disk space, please ensure your current disk space are bigger than 2G.");
				isLegal = false;
			} else {
				File dir = new File("tmp");
				if (!dir.exists()) {
					dir.mkdir();
				}
				Property.createInstance(bugFilePath, sourceCodeDir, dir
						.getAbsolutePath(), alpha, outputFile, projectStr, sourceCodeDir.length());
			}

		}

		return isLegal;
	}
}