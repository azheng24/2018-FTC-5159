/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.punabots.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


/**

 */


@TeleOp(name="TestBed v01elev", group="TestBed5159")
//@Disabled
public class T_TestBed_5159_v00 extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareTestBed_5159_v00 robot = new HardwareTestBed_5159_v00();   // Use Omni-Directional drive system

    @Override
    public void runOpMode() {

        // Initialize the robot and navigation
        robot.initHardware(this);

        // Wait for the game to start (driver presses PLAY)v
        while (!isStarted()) {
            // Prompt User
            telemetry.addData(">", "Press start");
            telemetry.update();
        }
        robot.positionServosTeleop();
        robot.resetMotorsTeleop();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            moveRobot();
            moveElev();
            moveLoader();
            //moveIntake();




            telemetry.addData("GP1:", "LeftStick Y:%.2f RightStick Y:%.2f",
                    gamepad1.left_stick_y,gamepad1.right_stick_y);
            telemetry.addData("MtrPwr", "Left %.3f Right %.3f",
                    robot.mtrLeftBack.getPower(),robot.mtrRightBack.getPower());
            telemetry.update();


        }

        telemetry.addData(">", "Shutting Down. Bye!");
        telemetry.update();
    }






    void moveLoader() {
     /*            Y
     /*        X       B
     /*            A
     */

        //if got here, none of the dpads have been pressed
        //check the joysticks (the intakes may or may not be running)
/*
        if (gamepad1.dpad_up) {
            robot.srvoLoader.setPosition(robot.SERVOLOADERTRANSPORT);
        }
        else if (gamepad1.dpad_down){
            robot.srvoLoader.setPosition(robot.SERVOLOADERLOAD);
        }
        else {
            robot.srvoLoader.setPosition(robot.SERVOLOADERDUMP);
        }
*/
        // slew the servo, according to the rampUp (direction) variable.
        if (gamepad1.dpad_up) {
            // Keep stepping up until we hit the max value.
            robot.position += robot.INCREMENT ;
            if (robot.position >= robot.MAX_POS ) {
                robot.position = robot.MAX_POS;
            //    robot.rampUp = !robot.rampUp;   // Switch ramp direction
            }
        }
        else if (gamepad1.dpad_down){
            // Keep stepping down until we hit the min value.
            robot.position -= robot.INCREMENT ;
            if (robot.position <= robot.MIN_POS ) {
                robot.position = robot.MIN_POS;
            //    robot.rampUp = !robot.rampUp;  // Switch ramp direction

            }
        }
        robot.srvoLoader.setPosition(robot.position);

        // Display the current value
        telemetry.addData("Servo Position", "%5.2f", robot.position);
        telemetry.addData(">", "Press Stop to end test." );
        telemetry.update();


        return;

    }




    /*
    void moveIntake() {
     */
     /*            Y
     /*        X       B
     /*            A
     */
/*
        if (gamepad1.y) {//if button y is pushed
            robot.mtrIntake.setPower(.99);
        }
        else if (gamepad1.a) {
             robot.mtrIntake.setPower(0);

        }


        return;
    }
    */



    void moveRobot() {

        if (gamepad1.left_stick_y < (-.5)) {
            robot.mtrLeftBack.setPower(.6);

        } else if (gamepad1.left_stick_y > (.5)) {
            robot.mtrLeftBack.setPower(-.6);
        } else {
            robot.mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.mtrLeftBack.setPower(0);
        }
        if (gamepad1.right_stick_y < -.5) {
            robot.mtrRightBack.setPower(.6);
        } else if (gamepad1.right_stick_y > (.5)) {
            robot.mtrRightBack.setPower(-.6);
        } else {
            robot.mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.mtrRightBack.setPower(0);
        }


    }
    /*  If you need help:
    if (gamepad1.right_stick_y < -.5) {
        robot.mtrRightBack.setPower(.99);
    } else if (gamepad1.right_stick_y > (.5)) {
        robot.mtrRightBack.setPower(-.99);
    } else {
       robot.mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrRightBack.setPower(0);
    }
    */

    void moveElev() {
     /*            Y
     /*        X       B
     /*            A
     */

        if (gamepad1.left_bumper) {
            robot.mtrElev.setPower(1);
        }
        else if (gamepad1.left_trigger>(.67)) {
            robot.mtrElev.setPower(-1);
        }
        else  {
            robot.mtrElev.setPower(0);
        }
        return;
    }

}


    
