package org.usfirst.frc.team192.robot;
import edu.wpi.first.wpilibj.command.Command;

public class Commands extends Command {

	/*
	 * 1.	Constructor - Might have parameters for this command such as target positions of devices. Should also set the name of the command for debugging purposes.
	 *  This will be used if the status is viewed in the dashboard. And the command should require (reserve) any devices is might use.
	 */
    public Commands() {
    	super("Commands");
        //requires(*insert name of mech here*);
    }

    // 	initialize() - This method sets up the command and is called immediately before the command is executed for the first time and every subsequent time it is started .
    //  Any initialization code should be here. 
    protected void initialize() {
    }

    /*
     *	execute() - This method is called periodically (about every 20ms) and does the work of the command. Sometimes, if there is a position a
     *  subsystem is moving to, the command might set the target position for the subsystem in initialize() and have an empty execute() method.
     */
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }
}
