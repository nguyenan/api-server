package com.wut.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.wut.support.settings.SettingsManager;

public class SystemHelper {
	
	public static boolean runTool(String toolName, String[] arguments) {
		String toolLocation = SettingsManager.getToolDirectory(toolName);
		String toolExe = SettingsManager.getToolExecutable(toolName);
		
		runCommand(toolLocation, toolExe, arguments, null);
			
		return true;
	}
	
	public static Process runProcess(String workingDirectoryStr, String command, String[] arguments, PrintStream output) {
		File workingDirectory = null;
		if (workingDirectoryStr != null) {
			workingDirectory = new File(workingDirectoryStr);
		}
		
		try {
            Runtime rt = Runtime.getRuntime();
            
            String argumentsString = StringHelper.combine(arguments, " ");
            
            String fullCommand = command + " " + argumentsString;
            System.out.println("In directory:" + workingDirectoryStr);
            System.out.println("Running command:" + fullCommand);
            Process pr = rt.exec(fullCommand, null, workingDirectory);

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;

            while ((line=input.readLine()) != null) {
                System.out.println(line);
               // output.append(line);
                if (output != null) {
                	output.append(line);
                }
                //.write(line.toCharArray())
            }
            
            // consume error stream otherwise process's buffer could get full and process may not finish
            BufferedReader error = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
            while ((line=error.readLine()) != null) {
                System.out.println(line);
            }

//            Runtime rt = Runtime.getRuntime();
//            Process proc = rt.exec("javac");
//            int exitVal = proc.exitValue(); // throws something if process is not done
//            System.out.println("Process exitValue: " + exitVal);
            
//            int exitVal = pr.waitFor();
//            System.out.println("Exited with error code "+exitVal);
            //String cmdOutput = String.valueOf(line);
            //System.out.println("OUTPUT:" + cmdOutput);
            
            return pr;
        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

		return null;
	}
	
	public static int runCommand(String workingDirectoryStr, String command, String[] arguments, PrintStream output) {
		//StringBuffer output = new StringBuffer();
		
		File workingDirectory = null;
		if (workingDirectoryStr != null) {
			workingDirectory = new File(workingDirectoryStr);
		}
		
		try {
            Runtime rt = Runtime.getRuntime();
            
            String argumentsString = StringHelper.combine(arguments, " ");
            
            String fullCommand = command + " " + argumentsString;
            System.out.println("In directory:" + workingDirectoryStr);
            System.out.println("Running command:" + fullCommand);
            Process pr = rt.exec(fullCommand, null, workingDirectory);

            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream(), "UTF-8"));

            String line=null;

            while ((line=input.readLine()) != null) {
                System.out.println(line);
               // output.append(line);
                if (output != null) {
                	output.append(line);
                }
                //.write(line.toCharArray())
            }

            int exitVal = pr.waitFor();
            System.out.println("Exited with error code "+exitVal);
            String cmdOutput = String.valueOf(line);
            System.out.println("OUTPUT:" + cmdOutput);
            
            return exitVal;
        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
			
		return -1;
	}
	
}
