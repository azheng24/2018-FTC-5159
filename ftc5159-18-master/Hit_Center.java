/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.punabots.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="Hit_Center", group="TestBed")
//@Disabled
public class Hit_Center extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareTestBed_5159_v00 robot       = new HardwareTestBed_5159_v00();
    Robot_Navigation_5159_v3 nav     = new Robot_Navigation_5159_v3();  // Use Image Tracking library
    private ElapsedTime     runtime = new ElapsedTime();


    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.5;
    static final double     TURN_SPEED              = 0.5;

    private static final int GOLD_LEFT = 0;
    private static final int GOLD_MIDDLE = 1;
    private static final int GOLD_RIGHT = 2 ;

    int nPos;

    @Override
    public void runOpMode() {
        long lMarkMilliS=System.currentTimeMillis();
        RelicRecoveryVuMark vuMark=null;
        boolean bTimeOut,bExit,bDistValid;
        String strColor="none";
        float fRedPct,fBluePct;

        float fHeading;
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.initHardware(this);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.mtrLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                          robot.mtrLeftBack.getCurrentPosition(),
                          robot.mtrRightBack.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        robot.srvoLoader.setPosition(0.1);


        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        elevator(7000, 50);

        //moving out from hook
        encoderDrive( 0.5,300,  300, 20.0);  // S1: Forward 47 Inches with 5 Sec timeout

        nPos=find_mineral(3000);
        switch(nPos) {
            case GOLD_LEFT:
                //turning towards ballsw
                encoderDrive(0.5,-650, 650, 20.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
                //hitting balls and go to depot
                encoderDrive( 0.5,5600,  5600, 20.0);
                //drop team marker
                robot.srvoLoader.setPosition(0.63);
                //wait
                sleep(500);
                break;
            case GOLD_MIDDLE:
                //turning towards ballsw
                encoderDrive(0.5,-700, 700, 20.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
                //hitting balls and go to depot
                encoderDrive( 0.5,5600,  5600, 20.0);
                //drop team marker
                robot.srvoLoader.setPosition(0.63);
                //wait
                sleep(500);

                break;
            case GOLD_RIGHT:
                //turning towards ballsw
                encoderDrive(0.5,-750, 700, 20.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
                //hitting balls and go to depot
                encoderDrive( 0.5,5600,  5600, 20.0);
                //drop team marker
                robot.srvoLoader.setPosition(0.63);
                //wait
                sleep(500);
                break;
            default:
                break;

        }


        //turning towards ballsw
        //encoderDrive(0.5,-700, 700, 20.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
        //hitting balls and go to depot
        encoderDrive( 0.5,5600,  5600, 20.0);
        //drop team marker
        robot.srvoLoader.setPosition(0.63);
        //wait
        sleep(500);

        // turn to face crater
        //encoderDrive( 0.5,1200,  -1200, 20.0);
        //drive towards crater
        //encoderDrive( 0.5,6000,   6000, 20.0);




        //robot.leftClaw.setPosition(1.0);            // S4: Stop and close the claw.
        //robot.rightClaw.setPosition(0.0);
        //sleep(1000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double speed,
                             int leftInches, int rightInches,
                             double timeout) {

        long lMarkMilliS;

        //Left Motor
        robot.mtrLeftBack.setDirection(DcMotor.Direction.REVERSE);// Positive input rotates counter clockwise
        robot.mtrLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrLeftBack.setTargetPosition(leftInches);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.mtrLeftBack.setPower(speed);

        //Right Motor
        robot.mtrRightBack.setDirection(DcMotor.Direction.FORWARD);// Positive input rotates counter clockwise
        robot.mtrRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrRightBack.setTargetPosition(rightInches);
        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.mtrRightBack.setPower(speed);

        lMarkMilliS=System.currentTimeMillis();
        while(((System.currentTimeMillis() - lMarkMilliS) < timeout*1000) && opModeIsActive() && robot.mtrLeftBack.isBusy() && robot.mtrRightBack.isBusy()) {
            telemetry.addData("Motor pos:", "%d, %d", robot.mtrLeftBack.getCurrentPosition(), robot.mtrRightBack.getCurrentPosition());
            telemetry.update();
        }
        robot.mtrLeftBack.setPower(0);
        robot.mtrRightBack.setPower(0);


/*
        int newLeftTarget;
        int newRightTarget;



        // Ensure that the opmode is still active
        if (opModeIsActive()) {


            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.mtrLeftBack.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.mtrRightBack.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.mtrLeftBack.setTargetPosition(newLeftTarget);
            robot.mtrRightBack.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.mtrLeftBack.setPower(Math.abs(speed));
            robot.mtrRightBack.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                   (runtime.seconds() < timeout) &&
                   (robot.mtrLeftBack.isBusy() && robot.mtrRightBack.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                                            robot.mtrLeftBack.getCurrentPosition(),
                                            robot.mtrRightBack.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.mtrLeftBack.setPower(0);
            robot.mtrRightBack.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
*/

    }


    // elevator
    public void elevator(int distance, int timeout){
        long lMarkMilliS=System.currentTimeMillis();
        boolean bTimeOut,bExit,bDistValid;
        robot.mtrElev.setDirection(DcMotor.Direction.REVERSE);// Positive input rotates counter clockwise
        robot.mtrElev.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrElev.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrElev.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrElev.setTargetPosition(distance);
        robot.mtrElev.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lMarkMilliS=System.currentTimeMillis();
        bTimeOut=false;
        robot.mtrElev.setPower(0.5);
        while((!bTimeOut)&&(opModeIsActive())&& robot.mtrElev.isBusy()){
            telemetry.addData("Elevator position:", "el:%d", robot.mtrElev.getCurrentPosition());
            telemetry.update();
            if ((System.currentTimeMillis() - lMarkMilliS) > timeout*1000) bTimeOut = true;
        }
        robot.mtrElev.setPower(0);
    }

    public int find_mineral(long lTimeToScan) {
        return GOLD_LEFT;
        //return GOLD_MIDDLE;
        //return GOLD_RIGHT;
    }
}
