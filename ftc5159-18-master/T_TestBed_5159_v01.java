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
import com.qualcomm.robotcore.hardware.Gamepad;


/**

 */


@TeleOp(name="TestBed v01Elev", group="TestBed5159")
//@Disabled
public class T_TestBed_5159_v01 extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareTestBed_5159_v01 robot = new HardwareTestBed_5159_v01();   // Use Omni-Directional drive system
    // Declaring class variables used throughout the program
    double dPwrFront = 0.2;
    double dPwrBack = 0.225;
    int nElevPreset = 0;
    double dPwrElevPreset = 0.2;
    int nElevPresetMax = 1000;
    double dPwrElev = 0.2;
    @Override
    public void runOpMode() {

        // Initialize the robot and navigation
        robot.initHardware(this);

        // Wait for the game to start (driver presses PLAY)
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
            moveElev(gamepad1);
            //moveLoader();
            //moveIntake();



            //display joystick position Y
            telemetry.addData("GP1-yPosition", "LeftStick Y:%.2f RightStick Y:%.2f",
                    gamepad1.left_stick_y,gamepad1.right_stick_y);
            //display joystick Position X
            telemetry.addData("GP1-xPosition:", "LeftStick X:%.2f RightStick X:%.2f",
                    gamepad1.left_stick_x,gamepad1.right_stick_x);
            //display motor LeftFront and motor RightFront power
            telemetry.addData("MtrPwr", "LF %.3f RF %.3f",
                    robot.mtrLeftFront.getPower(),robot.mtrRightFront.getPower());
            //display motor LeftBack and RightBack power
            telemetry.addData("MtrPwr", "LB %.3f RB %.3f",
                    robot.mtrLeftBack.getPower(),robot.mtrRightBack.getPower());
            //display Elevator power and CurrentPosition(encoder count)
            telemetry.addData("Elev", "Power %.3f  Pos %d",
                    robot.mtrElev.getPower(), robot.mtrElev.getCurrentPosition());
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


    // Drive Robot
    // Press buttons to change speed
    //   Y: slow
    //   B: medium
    //   A: fast
    // Move joysticks to change direction
    //   Forward: both joysticks forward
    //   Backward: both joysticks backward
    //   Turning left: right joystick goes forward, left joystick goes backward
    //   Turning right: left joystick goes backwards. right joysitck goes forward
    //   Strafe left: both joysticks to the left
    //   Strafe right: both joysticks to the right
    void moveRobot() {
        // Changes the speed by adjusting the power based on the button pressed
        // if Y is pressed, set robot speed to slow
        if ((gamepad1.y)) {
            dPwrFront = 0.2;
            dPwrBack = 0.225;
        }
        // if B is pressed, set robot speed to medium
        if ((gamepad1.b)) {
            dPwrFront = 0.4;
            dPwrBack = 0.43;
        }
        // if A is pressed, set robot speed to fast
        if ((gamepad1.a)) {
            dPwrFront = 0.6;
            dPwrBack =0.63;
        }

        // Driving forward and backwards based on the Y positions of both left and right joysticks
        // Makes sure x axis of the joysticks stays at the center and does not go beyond the halfway point
        // After the y axis of the joysticks goes beyond the halfway point, the power is applied
        // The left joystick controls the two left wheels and the right joystick controls the two right wheels
        if ((gamepad1.left_stick_x > -.5 && gamepad1.left_stick_x < .5) && (gamepad1.right_stick_x > -.5 && gamepad1.right_stick_x < .5)) {
            // left wheel
            if (gamepad1.left_stick_y < -.5) {
                // left motors go forward, if the joystick goes beyond the up halfway point
                robot.mtrLeftBack.setPower(dPwrFront);
                robot.mtrLeftFront.setPower(dPwrFront);
            } else if (gamepad1.left_stick_y > .5) {
                // left motors go backward, if the joystick goes beyond the down halfway point
                robot.mtrLeftBack.setPower(-dPwrFront);
                robot.mtrLeftFront.setPower(-dPwrFront);
            } else {
                // brake
                robot.mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrLeftBack.setPower(0);
                robot.mtrLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrLeftFront.setPower(0);
                robot.mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrRightBack.setPower(0);
                robot.mtrRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrRightFront.setPower(0);
            }
            // right wheel
            if (gamepad1.right_stick_y < -.5) {
                // right motors go forward, if the joystick goes beyond the up halfway point
                robot.mtrRightBack.setPower(dPwrFront);
                robot.mtrRightFront.setPower(dPwrFront);
            } else if (gamepad1.right_stick_y > 0.5) {
                // right motors go backwards, if the joystick goes beyond the down halfway point
                robot.mtrRightBack.setPower(-dPwrFront);
                robot.mtrRightFront.setPower(-dPwrFront);
            } else {
                // brake
                robot.mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrLeftBack.setPower(0);
                robot.mtrLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrLeftFront.setPower(0);
                robot.mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrRightBack.setPower(0);
                robot.mtrRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrRightFront.setPower(0);
            }
        }

        // Strafing left and right based on the X positions of both left and right joysticks
        // Makes sure y axis of the joysticks stays at the center and does not go beyond the halfway point
        // After the x axis of the joystick goes beyond the halfway point, the power is applied
        else if ((gamepad1.left_stick_y > -.5 && gamepad1.left_stick_y < .5) && (gamepad1.right_stick_y > -.5 && gamepad1.right_stick_y < .5)
                && (gamepad1.left_stick_x < -.5 && gamepad1.right_stick_x < -.5)) {
            // strafes the robot left, if both left and right joysticks go beyond the left halfway point
            robot.mtrLeftBack.setPower(dPwrBack);
            robot.mtrLeftFront.setPower(-dPwrFront);
            robot.mtrRightBack.setPower(-dPwrBack);
            robot.mtrRightFront.setPower(dPwrFront);
        } else if ((gamepad1.left_stick_y > -.5 && gamepad1.left_stick_y < .5) && (gamepad1.right_stick_y > -.5 && gamepad1.right_stick_y < .5)
                && (gamepad1.left_stick_x > .5 && gamepad1.right_stick_x > .5)) {
            // strafes the robot right, if both left and right joysticks go beyond the right halfway point
            robot.mtrLeftBack.setPower(-dPwrBack);
            robot.mtrLeftFront.setPower(dPwrFront);
            robot.mtrRightBack.setPower(dPwrBack);
            robot.mtrRightFront.setPower(-dPwrFront);
        } else {
            // brake if the both left and right joystick's x axis is with in the center
            robot.mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.mtrLeftBack.setPower(0);
            robot.mtrLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.mtrLeftFront.setPower(0);
            robot.mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.mtrRightBack.setPower(0);
            robot.mtrRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.mtrRightFront.setPower(0);


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

    //moving the elevator
    void moveElev(Gamepad gamepad) {
     //            Y
     //        X       B
     //            A
     //
        int nPosition = robot.mtrElev.getCurrentPosition();

        // if Elev position is out of range, stop power
        if (nPosition > nElevPresetMax || nPosition <= 0) {
            robot.mtrElev.setPower(0);
            nElevPreset = 0;
        }
        //manual control
        if (gamepad.left_bumper) {
            robot.mtrElev.setPower(dPwrElev);
            nElevPreset = 0;
            return;
        } else if (gamepad.left_trigger>(0.67)) {
            robot.mtrElev.setPower(-dPwrElev);
            nElevPreset = 0;
            return;
        } else {
// when buttons are pressed go to position
            if (gamepad.y) {
                if (nPosition < (nElevPresetMax)) {
                    nElevPreset = 1;
                    robot.mtrElev.setPower(dPwrElevPreset);
                }
            } else if (gamepad.a) {
                if (nPosition > (0)) {
                    nElevPreset = -1;
                    robot.mtrElev.setPower(-dPwrElevPreset);
                }
            }
        }
        if (nElevPreset == 0) {
            robot.mtrElev.setPower(0);
        }
        return;
    }



}


    
