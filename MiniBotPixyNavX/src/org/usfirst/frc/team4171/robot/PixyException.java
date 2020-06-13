package org.usfirst.frc.team4171.robot;
/**
 * 
 * @author 2B || !2B from https://www.chiefdelphi.com/forums/showpost.php?p=1443536&postcount=7
 * This class handles exceptions thrown from the pixy(ie. null pointers)
 */
@SuppressWarnings("serial")
public class PixyException extends Exception {
		
	public PixyException(String message){
		
		super(message);
	
	}

}