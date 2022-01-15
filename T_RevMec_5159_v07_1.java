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


@TeleOp(name="Teleop RevMec v07_1", group="RevMec5159")
//@Disabled
public class T_RevMec_5159_v07_1 extends LinearOpMode {
    //long lStoreTimeStamp = System.currentTimeMillis();

    /* Declare OpMode members. */
    HardwareRevMec_5159_v07 robot = new HardwareRevMec_5159_v07();   // Use Omni-Directional drive system



    @Override
    public void runOpMode() {

        int nIntakeLR = robot.INTAKELEFT;
        int nSpeedDir=robot.FASTFWD; // + or - indicates robot direction, 1 =

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

            nSpeedDir=moveRobot(nSpeedDir);
            nIntakeLR = moveIntake(nIntakeLR);
            moveElev();
            movePivot(400);
            moveLift();

            //resetServoPos();
            telemetry.addData("GP1:", "LeftStick Y:%.2f RightStick Y:%.2f",
                    gamepad1.left_stick_y,gamepad1.right_stick_y);
            telemetry.addData("GP1:", "LeftStick X:%.2f RightStick X:%.2f",
                    gamepad1.left_stick_x,gamepad1.right_stick_x);
            telemetry.addData("MtrPwr", "LF %.3f RF %.3f",
                    robot.mtrLeftFront.getPower(),robot.mtrRightFront.getPower());
            telemetry.addData("MtrPwr", "LB %.3f RB %.3f",
                    robot.mtrLeftBack.getPower(),robot.mtrRightBack.getPower());
            telemetry.addData("Pivot", "L:%d->%d(%s) R:%d->%d(%s)",
                    robot.mtrPivotL.getCurrentPosition(),robot.mtrPivotL.getTargetPosition(),
                    robot.mtrPivotL.isBusy()?"working":"done",
                    robot.mtrPivotR.getCurrentPosition(),robot.mtrPivotR.getTargetPosition(),
                    robot.mtrPivotR.isBusy()?"working":"done");
            telemetry.addData("Lift","Pos:%d",robot.mtrLift.getCurrentPosition());
            telemetry.addData("Elevator","Pos:%d",robot.mtrElev.getCurrentPosition());

            telemetry.update();


        }

        telemetry.addData(">", "Shutting Down. Bye!");
        telemetry.update();
    }




    int moveIntake(int nIntakeSetting) {
     // dpad
     //            Up
     //      Left     Right
     //           Down


        //if got here, none of the dpads have been pressed
        //check the joysticks (the intakes may or may not be running)

        if (gamepad2.dpad_left) {
            return (robot.INTAKELEFT);  //grabber setting left
        }
        else if(gamepad2.dpad_right) {
            return (robot.INTAKERIGHT);
        }
        else if(gamepad2.dpad_up) {
            if(nIntakeSetting ==robot.INTAKELEFT) { //left selected
                robot.crsrvoInL.setPower(robot.INTAKELFORWARD);
            }
            else {  //right selected
                robot.crsrvoInR.setPower(robot.INTAKERFORWARD);
            }

        }
        else if(gamepad2.dpad_down) {
            if (nIntakeSetting == robot.INTAKELEFT) { //left selected
                robot.crsrvoInL.setPower(robot.INTAKELREVERSE);
            } else {  //right selected
                robot.crsrvoInR.setPower(robot.INTAKERREVERSE);
            }
        }
        else{
            robot.crsrvoInL.setPower(robot.INTAKELSTOP);
            robot.crsrvoInR.setPower(robot.INTAKERSTOP);
        }
        return (nIntakeSetting);  //no change in grabber setting

    }


    void moveElev() {
        //if got here, none of the dpads have been pressed
        //check the joysticks (the intakes may or may not be running)
        if (gamepad2.right_bumper) {
            robot.mtrElev.setPower(1);
        }
        else if (gamepad2.right_trigger>.5) {
            robot.mtrElev.setPower(-1);
        }
        else  {
            robot.mtrElev.setPower(0);
        }
        return;
    }

    void movePivot(int nInterval) {
     /*            Y
     /*        X       B
     /*            A
     */
        //if got here, none of the dpads have been pressed

        //check if stuck


        if(robot.mtrPivotL.isBusy() || robot.mtrPivotR.isBusy()) return;

        if (gamepad2.y) {
            robot.mtrPivotL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.mtrPivotR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            robot.mtrPivotL.setTargetPosition(nInterval);
            robot.mtrPivotR.setTargetPosition(nInterval);
            robot.mtrPivotL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.mtrPivotR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.mtrPivotL.setPower(1);
            robot.mtrPivotR.setPower(1);
        }
        else if (gamepad2.a) {
            robot.mtrPivotL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.mtrPivotR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


            robot.mtrPivotL.setTargetPosition(-nInterval);
            robot.mtrPivotR.setTargetPosition(-nInterval);
            robot.mtrPivotL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.mtrPivotR.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.mtrPivotL.setPower(-1);
            robot.mtrPivotR.setPower(-1);
        }
        else  {
            //dont turn off power, running to position
            //robot.mtrPivotL.setPower(0);
            //robot.mtrPivotR.setPower(0);
        }
        return;
    }

    void moveLift() {

        //if got here, none of the dpads have been pressed
        //check the joysticks (the intakes may or may not be running)
        if (gamepad1.left_bumper) {
            robot.mtrLift.setPower(1);
        }
        else if (gamepad1.left_trigger>.5) {
            robot.mtrLift.setPower(-1);
        }
        else  {
            robot.mtrLift.setPower(0);
        }
        return;
    }


    int moveRobot(int nSpeedDir) {

        double dPwr;
        boolean bFoundLeftInput=false, bFoundRightInput=false;

        //                               Y
        //                           X       B
        //                               A
        if(gamepad1.x) {//hit x, slow speed setting
            if(nSpeedDir==robot.FASTFWD) return robot.SLOWFWD;
            else if(nSpeedDir==robot.FASTREV) return robot.SLOWREV;
            return nSpeedDir; //no change in speed, so just return what was sent;
        }
        else if(gamepad1.b) {//hit b, fast speed setting
            if(nSpeedDir==robot.SLOWFWD) return robot.FASTFWD;
            else if(nSpeedDir==robot.SLOWREV) return robot.FASTREV;
            return nSpeedDir; //no change in speed, so just return what was sent;
        }
        else if(gamepad1.y) {//hit y, move in forward direction
            if(nSpeedDir==robot.SLOWREV) return robot.SLOWFWD;
            else if(nSpeedDir==robot.FASTREV) return robot.FASTFWD;
            return nSpeedDir; //no change in speed, so just return what was sent;
        }
        else if(gamepad1.a) {//hit y, move in forward direction
            if(nSpeedDir==robot.SLOWFWD) return robot.SLOWREV;
            else if(nSpeedDir==robot.FASTFWD) return robot.FASTREV;
            return nSpeedDir; //no change in speed, so just return what was sent;
        }


        //if got here not buttons hit so, can set power
        if((nSpeedDir==robot.FASTFWD) || (nSpeedDir==robot.FASTREV))
           dPwr = robot.FULLPOWER;
        else //slow power required
           dPwr = robot.SLOWPOWER;


        //now apply power
        //check left y
        ///CAUTION!!! Joysticks front/back are REVERSED!!!! left/right is okay-
        //     -          -
        //  -  L  +    -  R  +
        //     +          +

        //gamepad1: Drive train & lift
        //gamepad2: Elevator, claw, pivot
        if (gamepad1.left_stick_y < (-.8)) {  //left stick moved UP!!
            if((nSpeedDir==robot.FASTFWD) || (nSpeedDir==robot.SLOWFWD)) {
                robot.mtrLeftBack.setPower(dPwr);  //forward
                robot.mtrLeftFront.setPower(dPwr); //forward
            }
            else { //robot in reverse mode
                robot.mtrRightBack.setPower(-dPwr);  //backward
                robot.mtrRightFront.setPower(-dPwr); //backward
            }
            bFoundLeftInput=true;
        }
        if (gamepad1.left_stick_y > (.8)) {  //left stick moved DOWN!!
            if((nSpeedDir==robot.FASTFWD) || (nSpeedDir==robot.SLOWFWD)) {
               robot.mtrLeftBack.setPower(-dPwr);  //backward
               robot.mtrLeftFront.setPower(-dPwr); //backward
            }
            else {  //robot in reverse mode
                robot.mtrRightBack.setPower(dPwr);  //forward
                robot.mtrRightFront.setPower(dPwr); //forward
            }
            bFoundLeftInput=true;
        }
        //check right y
        if (gamepad1.right_stick_y < (-.8)) { //right stick moved UP!!
            if((nSpeedDir==robot.FASTFWD) || (nSpeedDir==robot.SLOWFWD)) {
                robot.mtrRightBack.setPower(dPwr);  //forward
                robot.mtrRightFront.setPower(dPwr);  //forward
            }
            else { //robot in reverse mode
                robot.mtrLeftBack.setPower(-dPwr);  //backward
                robot.mtrLeftFront.setPower(-dPwr);  //backward
            }
            bFoundRightInput=true;
        }
        if (gamepad1.right_stick_y > (.8)) {  //right stick moved DOWN
            if((nSpeedDir==robot.FASTFWD) || (nSpeedDir==robot.SLOWFWD)) {
                robot.mtrRightBack.setPower(-dPwr);  //backward
                robot.mtrRightFront.setPower(-dPwr);  //backward
            }
            else {
                robot.mtrLeftBack.setPower(dPwr);  //forward
                robot.mtrLeftFront.setPower(dPwr);  //forward
            }
            bFoundRightInput=true;
        }
        //now check left-right lateral movement
        if (gamepad1.left_stick_x < -.8){  //left stick moved LEFT
            if((nSpeedDir==robot.FASTFWD) || (nSpeedDir==robot.SLOWFWD)) {
                robot.mtrLeftBack.setPower(dPwr);
                robot.mtrLeftFront.setPower(-dPwr);
            }
            else {
                robot.mtrRightBack.setPower(dPwr);
                robot.mtrRightFront.setPower(-dPwr);
            }
            bFoundLeftInput = true;
        }
        if (gamepad1.right_stick_x < -.8) {  //right stick moved LEFT
            if((nSpeedDir==robot.FASTFWD) || (nSpeedDir==robot.SLOWFWD)) {
                robot.mtrRightBack.setPower(-dPwr);
                robot.mtrRightFront.setPower(dPwr);
            }
            else {
                robot.mtrLeftBack.setPower(-dPwr);
                robot.mtrLeftFront.setPower(dPwr);
            }
            bFoundRightInput=true;
        }
        if (gamepad1.left_stick_x > .8) {  //left stick moved RIGHT
            if((nSpeedDir==robot.FASTFWD) || (nSpeedDir==robot.SLOWFWD)) {
                robot.mtrLeftBack.setPower(-dPwr);
                robot.mtrLeftFront.setPower(dPwr);
            }
            else {
                robot.mtrRightBack.setPower(-dPwr);
                robot.mtrRightFront.setPower(dPwr);
            }
            bFoundLeftInput=true;
        }

        if (gamepad1.right_stick_x > .8) { //right stick moved RIGHT
            if((nSpeedDir==robot.FASTFWD) || (nSpeedDir==robot.SLOWFWD)) {
                robot.mtrRightBack.setPower(dPwr);
                robot.mtrRightFront.setPower(-dPwr);
            }
            else {
                robot.mtrLeftBack.setPower(dPwr);
                robot.mtrLeftFront.setPower(-dPwr);
            }
           bFoundRightInput=true;

        }

        //now turn off motors if no input
        if(!bFoundLeftInput) {
            if((nSpeedDir==robot.FASTFWD) || (nSpeedDir==robot.SLOWFWD)) {
                robot.mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrLeftBack.setPower(0);
                robot.mtrLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrLeftFront.setPower(0);
            }
            else {
                robot.mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrRightBack.setPower(0);
                robot.mtrRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrRightFront.setPower(0);
            }
        }

        if(!bFoundRightInput) {
            if((nSpeedDir==robot.FASTFWD) || (nSpeedDir==robot.SLOWFWD)) {
                robot.mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrRightBack.setPower(0);
                robot.mtrRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrRightFront.setPower(0);
            }
            else {
                robot.mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrLeftBack.setPower(0);
                robot.mtrLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.mtrLeftFront.setPower(0);
            }
        }
        return nSpeedDir;
    }

}


    
