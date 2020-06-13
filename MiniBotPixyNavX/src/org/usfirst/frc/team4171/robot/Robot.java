package org.usfirst.frc.team4171.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class Robot extends IterativeRobot {
	
	RobotDrive myRobot;
	XboxController controller = new XboxController(0);
	
//	final String defaultAuto = "Default";
//	final String customAuto = "My Auto";
//	String autoSelected;
//	SendableChooser<String> chooser = new SendableChooser<>();

	//Channels for wheels
	final int kLeftChannel = 1;
	final int kRightChannel = 0;
	
	//Constructs values for driving
	double motorXValue;
	double motorYValue;
	double iterationY;
	double iterationX;
	double absYValue;
	
	//Timer
	Timer timer = new Timer();
	
	//Pixy Junk
	public PixyI2C pixy;
	Port port = Port.kOnboard;
	String print;
	public PixyPacket[] packet = new PixyPacket[7];
	
	//NavX System
	
	@Override
	public void robotInit() {
		
		//Robot Drive
		myRobot = new RobotDrive(kLeftChannel, kRightChannel);
		
		//Pixy Cam
		pixy = new PixyI2C("pixy", new I2C(port, 0x54), packet, new PixyException(print), new PixyPacket());
		System.out.println(pixy.name);
//		chooser.addDefault("Default Auto", defaultAuto);
//		chooser.addObject("My Auto", customAuto);
//		SmartDashboard.putData("Auto choices", chooser);
		
	}

	@Override
	public void teleopInit() {
		
		testPixy();
		
	}
	
	@Override
	public void autonomousInit() {

//		autoSelected = chooser.getSelected();
//		// autoSelected = SmartDashboard.getString("Auto Selector",
//		// defaultAuto);
//		System.out.println("Auto selected: " + autoSelected);
	
	}

	@Override
	public void autonomousPeriodic() {
		
//		switch (autoSelected) {
//		case customAuto:
//			// Put custom auto code here
//			break;
//		case defaultAuto:
//		default:
//			// Put default auto code here
//			break;
//		}
	
	}

	@Override
	public void teleopPeriodic() {
	
		if (controller.getY(Hand.kLeft) != 0) {
			
			iterationY = iterationY + 0.2;
			absYValue = Math.abs(controller.getY(Hand.kLeft));
			motorYValue = (absYValue)/(1+((absYValue-(0.001))/(0.001))*(Math.pow(2.8, (-(0.5)*(iterationY)))));
			
			if(controller.getY(Hand.kLeft) < 0) motorYValue = -(motorYValue);
		
		} else {
			motorYValue = 0;
			iterationY = 0;
			absYValue = 0;
		} 
		
		Timer.delay(0.005);
		
		motorXValue = controller.getX(Hand.kLeft) / 1.2;
		
		myRobot.arcadeDrive(-(motorYValue), -(motorXValue), true);
	
		//Now here's where it starts getting weird.
		
		if (controller.getAButton()) getPegPosition();
		
	}

	@Override
	public void testPeriodic() {
		
	}

	public void testPixy() {
		
		for (int i = 0; i < packet.length; i++) packet[i] = null;
		
		SmartDashboard.putString("gearPixy hello", "working");
		
		for (int i = 1; i < 8; i++) {
			
			try {packet[i - 1] = pixy.readPacket(i);} 
			catch (PixyException e) {SmartDashboard.putString("gearPixy Error: " + i, "exception");}
			
			if (packet[i - 1] == null) {
				SmartDashboard.putString("gearPixy Error: " + i, "True");
				continue;
			}
			
			SmartDashboard.putNumber("gearPixy X Value: " + i, packet[i - 1].X);
			SmartDashboard.putNumber("gearPixy Y Value: " + i, packet[i - 1].Y);
			SmartDashboard.putNumber("gearPixy Width Value: " + i, packet[i - 1].Width);
			SmartDashboard.putNumber("gearPixy Height Value: " + i, packet[i - 1].Height);
			SmartDashboard.putString("gearPixy Error: " + i, "False");
			
		}
		
	}
	
	// Get blocks that represent the vision tape on either side of the peg. This
	// can return 0,1, or 2 blocks depending what is found in a frame.
	public PixyPacket[] getPegPosition() {
		
		PixyPacket[] blocks = pixy.readBlocks();
		SmartDashboard.putBoolean("Peg Blocks Array is null", blocks == null);
		if (blocks == null)
			return null;
		SmartDashboard.putString("Peg Block 0", (blocks[0] == null) ? "null" : blocks[0].toString());
		SmartDashboard.putString("Peg Block 1", (blocks[1] == null) ? "null" : blocks[1].toString());
		return blocks;
	
	}

	
	
}

