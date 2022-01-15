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
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaNavigation;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_GOLD_MINERAL;

/**
 * This OpMode illustrates the basics of using the Vuforia engine to determine
 * the identity of Vuforia VuMarks encountered on the field. The code is structured as
 * a LinearOpMode. It shares much structure with {@link ConceptVuforiaNavigation}; we do not here
 * duplicate the core Vuforia documentation found there, but rather instead focus on the
 * differences between the use of Vuforia for navigation vs VuMark identification.
 *
 * @see ConceptVuforiaNavigation
 * @see VuforiaLocalizer
 * @see VuforiaTrackableDefaultListener
 * see  ftc_app/doc/tutorial/FTC_FieldCoordinateSystemDefinition.pdf
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained in {@link ConceptVuforiaNavigation}.
 */

@Autonomous(name="Auton RevMec v08_01", group ="RevMec")
//@Disabled
public class A_RevMec_5159_v08_01 extends LinearOpMode {

    //public static final String TAG = "Vuforia VuMark Sample";
    //OpenGLMatrix lastLocation = null;

    private static final int GOLD_LEFT = 0;
    private static final int GOLD_MIDDLE = 1;
    private static final int GOLD_RIGHT = 2 ;

    /* Declare OpMode members. */
    HardwareRevMec_5159_v07 robot       = new HardwareRevMec_5159_v07();
    VisionProcessing_5159_v1 visproc     = new VisionProcessing_5159_v1();  // Use Image Tracking library


    @Override public void runOpMode() {
        long lMarkMilliS = System.currentTimeMillis();
        int nPos = 0;
        String strPos="";


        float fHeading;

        // Initialize the robot and navigation
        robot.initHardware(this);

        //setup image finding
        telemetry.addData("Status:", "Setting up Vuforia");
        telemetry.update();

        telemetry.addData("Status:", "Positioning servos");
        telemetry.update();
        robot.positionServosAuton();

        telemetry.addData("Status:", "Reset motors");
        telemetry.update();
        robot.resetMotorsAuton();

        telemetry.addData("Status:", "Settting up Visual Processing");
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        visproc.initVuforia();
        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            visproc.initTfod(this);
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
        telemetry.update();


        telemetry.addData(">", "What is life.");
        telemetry.update();
        waitForStart();
        if (!opModeIsActive()) return;


        landRobot(-2000, 17000);
        moveLeft(200, .75, 15000);
        moveForward(400, .75, 7000);
        //moveRight(400,.85, 15000)

        nPos=findMineral(3000);
        switch(nPos) {
            case GOLD_LEFT:
                strPos = "Left";
                break;
            case GOLD_MIDDLE:
                strPos = "Middle";
                moveForward(800,.75,1000);
                extendElev(2000,1000);
                releaseMarker(-100);
                extendElev(200,1000);

                break;
            case GOLD_RIGHT:
                strPos = "Right";
                //moveRight(300,.75,1000);
                break;
            default:
                strPos = "Err";
                break;

        }
        //extendElev(2128,15000);
        //releaseMarker(2000);
        //robot.turnLeftHeading(100,this,15000);
        //pivot_right(50,.5,1000);


        while (((System.currentTimeMillis() - lMarkMilliS) < 30000) && (opModeIsActive())) {
            telemetry.addData("Status:", "Heading:%.2f", robot.getHeading());
            telemetry.addData("Range", "L:%.01f mm  R:%.01f mm",
                    robot.sensorRangeL.getDistance(DistanceUnit.MM),
                    robot.sensorRangeR.getDistance(DistanceUnit.MM));

            telemetry.addData("Prox:", "L:%.2f  R:%.2f",
                    robot.sensProxMineralL.getDistance(DistanceUnit.CM),
                    robot.sensProxMineralR.getDistance(DistanceUnit.CM)
            );
            /*
            telemetry.addData("   Alpha","value %d", robot.sensColorMineralL.alpha());
            telemetry.addData("  Red  ","value %d Pct %.02f", robot.sensColorMineralL.red(),
                    (float)robot.sensColorMineralL.red()/(float)robot.sensColorMineralL.alpha() );
            //telemetry.addData("Green", robot.sensColorMineralL.green());
            telemetry.addData("  Blue ","value %d Pct %.02f", robot.sensColorMineralL.blue(),
                    (float)robot.sensColorMineralL.blue()/(float)robot.sensColorMineralL.alpha() );
            Color.RGBToHSV((int) (robot.sensColorMineralL.red() * robot.SCALE_FACTOR),
                    (int) (robot.sensColorMineralL.green() * robot.SCALE_FACTOR),
                    (int) (robot.sensColorMineralL.blue() * robot.SCALE_FACTOR),
                    robot.hsvValuesL);
            telemetry.addData("   Hue", "%.2f",robot.hsvValuesL[0]);
            */
            telemetry.addData("Mtr:", "LF %d  RF %d", robot.mtrLeftFront.getCurrentPosition(), robot.mtrRightFront.getCurrentPosition());
            telemetry.addData("Mtr:", "LB %d  RB %d", robot.mtrLeftBack.getCurrentPosition(), robot.mtrRightBack.getCurrentPosition());
            telemetry.addData("Found:", "%d: %s ", nPos, strPos);
            telemetry.update();
            idle();
        }
    }

    int findMineral(long lTimeToScan){
        int nMineralCnt=0,nCnt;
        boolean bFndGold=false,bFndSilver1=false,bFndSilver2=false;
        float fGold=0,fSilver1=0,fSilver2=0;
        long lTimeStamp;

        if (!opModeIsActive()) return -1;

        /** Activate Tensor Flow Object Detection. */
        if (visproc.tfod != null) {
            visproc.tfod.activate();
        }
        lTimeStamp=System.currentTimeMillis();
        while (opModeIsActive()&&((System.currentTimeMillis()-lTimeStamp)<lTimeToScan)) {
            if (visproc.tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = visproc.tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    nMineralCnt = updatedRecognitions.size();
                    if (nMineralCnt > 0) {
                        bFndGold = bFndSilver1 = bFndSilver2 = false;
                        fGold=fSilver1=fSilver2=0;
                        nCnt = 0;
                        for (Recognition recognition : updatedRecognitions) { //go through each recognized object
                            telemetry.addData("   mineral ", "%d: %s", nCnt++, recognition.getLabel());
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                bFndGold = true;
                                fGold=recognition.getLeft();
                                telemetry.addData("found gold", "getleft=%.2f", fGold);
                            } else if (bFndSilver1 == false) { //found 1st silver mineral
                                bFndSilver1 = true;
                                fSilver1=recognition.getLeft();
                                telemetry.addData("found 1 silver", "getleft=%.2f",fSilver1);
                            } else { //found 2nd silver
                                bFndSilver2 = true;
                                fSilver2=recognition.getLeft();
                                telemetry.addData("found 2 silver", "getleft=%.2f", fSilver2);
                            }
                        }
                    }
                    telemetry.update();
                }
            }
        } //end while
        if (visproc.tfod != null) {
            visproc.tfod.shutdown();
        }
        if(opModeIsActive())  {
            if(nMineralCnt==2) {
                if(bFndGold==true) { //remember screen is inverted
                    //if got here, one gold  one silver
                   if(fGold>fSilver1) return GOLD_MIDDLE;
                   else return GOLD_RIGHT;
                }
                else {
                    //if got here, both are silver
                   return GOLD_LEFT;
                }
            }
            else return -1;
        }


        return 0;
    }

    void landRobot(int nTargetCount,long lTimeOutMilliS) {
        long lMarkMilliS;
        boolean bTimeOut;

        //raise elevator to drop bot
        robot.mtrLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrLift.setTargetPosition(nTargetCount);
        robot.mtrLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        lMarkMilliS=System.currentTimeMillis();
        bTimeOut=false;
        if(nTargetCount>0)
           robot.mtrLift.setPower(1);
        else
            robot.mtrLift.setPower(-1);
        while((!bTimeOut)&&(opModeIsActive())&&
                (robot.mtrLift.isBusy())){
            telemetry.addData("Lift", "%d", robot.mtrLift.getCurrentPosition());
            telemetry.update();
            if ((System.currentTimeMillis() - lMarkMilliS) > lTimeOutMilliS) bTimeOut = true;
        }

    }


    void extendElev(int nTargetCount,long lTimeOutMilliS) {
        long lMarkMilliS;
        boolean bTimeOut;

        robot.mtrElev.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrElev.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrElev.setTargetPosition(nTargetCount);
        robot.mtrElev.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        lMarkMilliS=System.currentTimeMillis();
        bTimeOut=false;
        if(nTargetCount>0)
            robot.mtrElev.setPower(1);
        else
            robot.mtrElev.setPower(-1);
        while((!bTimeOut)&&(opModeIsActive())&&
                (robot.mtrElev.isBusy())){
            telemetry.addData("ElevPos", "%d", robot.mtrElev.getCurrentPosition());
            telemetry.update();
            if ((System.currentTimeMillis() - lMarkMilliS) > lTimeOutMilliS) bTimeOut = true;
        }

    }

    void releaseMarker(long lTimeToReleaseMilliS) {
        long lMarkMilliS;


        robot.crsrvoInL.setPower(robot.INTAKELFORWARD);
        robot.crsrvoInR.setPower(robot.INTAKERFORWARD);

        lMarkMilliS=System.currentTimeMillis();

        while ((System.currentTimeMillis() - lMarkMilliS) < lTimeToReleaseMilliS) {
            idle();
        }
    }


    void moveForward(int nEncoderCount, double dPwr, long lTimeOutMilliS) {
        long lMarkMilliS=System.currentTimeMillis();
        //int nRFPos,nRBPos,nLFPos,nLBPos;

        //run a given distance
        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrLeftBack.setTargetPosition(nEncoderCount);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrRightBack.setTargetPosition(nEncoderCount);
        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrLeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrLeftFront.setTargetPosition(nEncoderCount);
        robot.mtrLeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrRightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrRightFront.setTargetPosition(nEncoderCount);
        robot.mtrRightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrLeftBack.setPower(dPwr);
        robot.mtrRightBack.setPower(dPwr);
        robot.mtrLeftFront.setPower(dPwr);
        robot.mtrRightFront.setPower(dPwr);


        while(opModeIsActive()) {
            /*
            if(!robot.mtrRightBack.isBusy() && !robot.mtrRightFront.isBusy()
               &&!robot.mtrLeftBack.isBusy() && !robot.mtrRightBack.isBusy()) break;
            */
            telemetry.addData("Mtr:","LF %d  RF %d", robot.mtrLeftFront.getCurrentPosition(),robot.mtrRightFront.getCurrentPosition());
            telemetry.addData("Mtr:","LB %d  RB %d", robot.mtrLeftBack.getCurrentPosition(),robot.mtrRightBack.getCurrentPosition());
            telemetry.update();
            if ((System.currentTimeMillis() - lMarkMilliS) > lTimeOutMilliS)  break;
            if(!robot.mtrRightBack.isBusy() || !robot.mtrRightFront.isBusy()
                    || !robot.mtrLeftBack.isBusy() || !robot.mtrRightBack.isBusy()) break;
        }
        // Stop all motion;
        robot.mtrRightBack.setPower(0);
        robot.mtrLeftBack.setPower(0);
        robot.mtrLeftFront.setPower(0);
        robot.mtrRightFront.setPower(0);

        //move all the motors to the RF Pos
        //nRFPos=robot.mtrRightFront.getCurrentPosition();
        //nRBPos=robot.mtrRightBack.getCurrentPosition();
        //nLFPos=robot.mtrLeftFront.getCurrentPosition();
        //nLBPos=robot.mtrLeftBack.getCurrentPosition();



        // Turn off RUN_TO_POSITION
        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        return;
    }

    void moveLeft(int nEncoderCount, double dPwr, long lTimeOutMilliS) {
        long lMarkMilliS=System.currentTimeMillis();


        //run a given distance
        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrLeftBack.setTargetPosition(nEncoderCount);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrRightBack.setTargetPosition(-nEncoderCount);
        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrLeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrLeftFront.setTargetPosition(-nEncoderCount);
        robot.mtrLeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrRightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrRightFront.setTargetPosition(nEncoderCount);
        robot.mtrRightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrLeftBack.setPower(dPwr);
        robot.mtrRightBack.setPower(-dPwr);
        robot.mtrLeftFront.setPower(-dPwr);
        robot.mtrRightFront.setPower(dPwr);


        while(opModeIsActive()) {
            /*
            if(!robot.mtrRightBack.isBusy() && !robot.mtrRightFront.isBusy()
               &&!robot.mtrLeftBack.isBusy() && !robot.mtrRightBack.isBusy()) break;
            */
            telemetry.addData("Mtr:","LF %d  RF %d", robot.mtrLeftFront.getCurrentPosition(),robot.mtrRightFront.getCurrentPosition());
            telemetry.addData("Mtr:","LB %d  RB %d", robot.mtrLeftBack.getCurrentPosition(),robot.mtrRightBack.getCurrentPosition());
            telemetry.update();
            if ((System.currentTimeMillis() - lMarkMilliS) > lTimeOutMilliS)  break;
            if(!robot.mtrRightBack.isBusy() || !robot.mtrRightFront.isBusy()
                    || !robot.mtrLeftBack.isBusy() || !robot.mtrRightBack.isBusy()) break;
        }
        // Stop all motion;
        robot.mtrRightBack.setPower(0);
        robot.mtrLeftBack.setPower(0);
        robot.mtrLeftFront.setPower(0);
        robot.mtrRightFront.setPower(0);


        // Turn off RUN_TO_POSITION
        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        return;
    }

    void moveRight(int nEncoderCount, double dPwr, long lTimeOutMilliS) {
        long lMarkMilliS=System.currentTimeMillis();


        //run a given distance
        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.mtrLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrLeftBack.setTargetPosition(-nEncoderCount);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrRightBack.setTargetPosition(nEncoderCount);
        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrLeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrLeftFront.setTargetPosition(nEncoderCount);
        robot.mtrLeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrRightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.mtrRightFront.setTargetPosition(-nEncoderCount);
        robot.mtrRightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrLeftBack.setPower(-dPwr);
        robot.mtrRightBack.setPower(dPwr*(double).9);
        robot.mtrLeftFront.setPower(dPwr);
        robot.mtrRightFront.setPower(-dPwr);


        while(opModeIsActive()) {
            /*
            if(!robot.mtrRightBack.isBusy() && !robot.mtrRightFront.isBusy()
               &&!robot.mtrLeftBack.isBusy() && !robot.mtrRightBack.isBusy()) break;
            */
            telemetry.addData("Mtr:","LF %d  RF %d", robot.mtrLeftFront.getCurrentPosition(),robot.mtrRightFront.getCurrentPosition());
            telemetry.addData("Mtr:","LB %d  RB %d", robot.mtrLeftBack.getCurrentPosition(),robot.mtrRightBack.getCurrentPosition());
            telemetry.update();
            if ((System.currentTimeMillis() - lMarkMilliS) > lTimeOutMilliS)  break;
            if(!robot.mtrRightBack.isBusy() || !robot.mtrRightFront.isBusy()
                    || !robot.mtrLeftBack.isBusy() || !robot.mtrRightBack.isBusy()) break;
        }
        // Stop all motion;
        robot.mtrRightBack.setPower(0);
        robot.mtrLeftBack.setPower(0);
        robot.mtrLeftFront.setPower(0);
        robot.mtrRightFront.setPower(0);


        // Turn off RUN_TO_POSITION
        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        return;
    }


    int guessColor(ColorSensor csMineral) {
        int nTotal=csMineral.red()+csMineral.green()+csMineral.blue();
        float fCalc= ((float) 100.0*(float)csMineral.red())/(float) nTotal;
        if(fCalc<=38.0) return 100; //even color dist=silver
        else return 200; //disproportional red means gold;
    }

    void pivot_right(int nEncoderTarget, double dPwr, long lTimeOutMilliS) {
        long lMarkMilliS=System.currentTimeMillis();

        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.mtrLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrLeftBack.setTargetPosition(nEncoderTarget);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrLeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrLeftFront.setTargetPosition(nEncoderTarget);
        robot.mtrLeftFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrRightBack.setTargetPosition(-nEncoderTarget);
        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.mtrRightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.mtrRightFront.setTargetPosition(-nEncoderTarget);
        robot.mtrRightFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        robot.mtrRightBack.setPower(-dPwr);
        robot.mtrLeftBack.setPower(dPwr);
        robot.mtrLeftFront.setPower(dPwr);
        robot.mtrRightFront.setPower(-dPwr);
        while((System.currentTimeMillis()-lMarkMilliS)<lTimeOutMilliS) {
            if(!robot.mtrRightBack.isBusy() && !robot.mtrRightFront.isBusy()
                    && !robot.mtrLeftBack.isBusy() && !robot.mtrRightBack.isBusy()){
                break;
            }

            idle();

        }
        // Stop all motion;
        robot.mtrRightBack.setPower(0);
        robot.mtrLeftBack.setPower(0);
        robot.mtrLeftFront.setPower(0);
        robot.mtrRightFront.setPower(0);
        // Turn off RUN_TO_POSITION
        robot.mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.mtrLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



    }

}





//get target for glyph
        /*
        telemetry.addData("Status:","Getting Key Column");
        telemetry.update();
        if((strResult=nav.get_key_column(relicTemplate,this,vuMark))=="not found") strMark="right";
        else strMark=strResult;
        telemetry.addData("Status:","Key Column:%s",strResult=="not found"?"NOT FOUND-using right":strMark);
        telemetry.update();
        */


//get color of jewel

//telemetry.addData("Status:","Getting BallColor");
//telemetry.update();

//strJewelColor=robot.getJewelColor(this);
//telemetry.addData("Status:","Key:%s; Jewel Color:%s",strMark,strJewelColor);
//telemetry.update();

/*
        //get off stone
        robot.goBackward(1150,.5,this,4000);
        if(robot.getHeading()<-3) {//pointing right, correct left
            robot.turnLeftHeadingFine(0,this,3000);
        }
        else if(robot.getHeading()>3) {//pointing left, correct right
            robot.turnRightHeadingFine(0,this,3000);
        }



        if ((strMark=="right")) {
            robot.goBackward(250,.8,this,4000);
            robot.turnLeftHeading(60,this,4000);//
            if(robot.getHeading()<57) {//pointing right, correct left for 90
                robot.turnLeftHeadingFine(60,this,2000);
            }
            else if(robot.getHeading()>63) {//pointing left, correct right for 90
                robot.turnRightHeadingFine(60,this,2000);
            }
            robot.goBackward(700, .9, this, 4000); //move to key column
            robot.srvoGlyphAlign.setPosition(robot.SERVOGLYPHALIGNOPEN);
            robot.dumpGlyphToBox(this,2000);
            robot.goForward(100, .3, this, 4000); //move to key column
            lMarkMilliS=System.currentTimeMillis();
            while(((System.currentTimeMillis()-lMarkMilliS)<1000)&&(opModeIsActive())) {
                idle();
            }
            robot.goForward(50, .3, this, 500);
            lMarkMilliS=System.currentTimeMillis();
            while(((System.currentTimeMillis()-lMarkMilliS)<1000)&&(opModeIsActive())) {
                idle();
            }
            robot.goForward(50, .3, this, 500);//move to key column
            lMarkMilliS=System.currentTimeMillis();
            while(((System.currentTimeMillis()-lMarkMilliS)<1000)&&(opModeIsActive())) {
                idle();
            }
            robot.goForward(200, .5, this, 4000); //move from key column
        }
        else if ((strMark=="center")) {
            robot.goForward(100,.8,this,4000);
            robot.turnLeftHeading(60,this,4000);//
            if(robot.getHeading()<62) {//pointing right, correct left for 90
                robot.turnLeftHeadingFine(65,this,2000);
            }
            else if(robot.getHeading()>68) {//pointing left, correct right for 90
                robot.turnRightHeadingFine(65,this,2000);
            }
            robot.goBackward(700, .9, this, 4000); //move to key column
            robot.srvoGlyphAlign.setPosition(robot.SERVOGLYPHALIGNOPEN);
            robot.dumpGlyphToBox(this,2000);
            robot.goForward(100, .3, this, 4000); //move to key column
            lMarkMilliS=System.currentTimeMillis();
            while(((System.currentTimeMillis()-lMarkMilliS)<1000)&&(opModeIsActive())) {
                idle();
            }
            robot.goForward(200, .5, this, 4000); //move from key column
        }
        else if ((strMark=="left")) {
            robot.goBackward(300,.5,this,4000);//300
            robot.turnLeftHeading(110,this,3000);//
            if(robot.getHeading()<107) {//pointing right, correct left for 90
                robot.turnLeftHeadingFine(110,this,2000);
            }
            else if(robot.getHeading()>113) {//pointing left, correct right for 90
                robot.turnRightHeadingFine(110,this,2000);
            }

            robot.goBackward(550, .9, this, 4000); //700move to key column

            robot.srvoGlyphAlign.setPosition(robot.SERVOGLYPHALIGNOPEN);

            robot.dumpGlyphToBox(this,2000);

            robot.goForward(50, .3, this, 500);
            lMarkMilliS=System.currentTimeMillis();
            while(((System.currentTimeMillis()-lMarkMilliS)<1000)&&(opModeIsActive())) {
                idle();
            }
            robot.goForward(50, .3, this, 500);//move to key column
            lMarkMilliS=System.currentTimeMillis();
            while(((System.currentTimeMillis()-lMarkMilliS)<1000)&&(opModeIsActive())) {
                idle();
            }

            robot.goForward(200, .5, this, 4000); //move from key column


        }

        //robot.turnRightHeading(-90,this,5000);
*/

/*
        robot.moveRampToDown(this,2000);
        robot.goBackward(500, .8, this, 1000);
        robot.goForward(200, .3, this, 2000);
        lMarkMilliS=System.currentTimeMillis();
        while(((System.currentTimeMillis()-lMarkMilliS)<1000)&&(opModeIsActive())) {
            idle();
        }
        robot.goForward(200, .3, this, 2000);//move to key column
        lMarkMilliS=System.currentTimeMillis();
        while(((System.currentTimeMillis()-lMarkMilliS)<1000)&&(opModeIsActive())) {
            idle();
        }
        robot.goForward(200, .3, this, 2000);//move to key column
        lMarkMilliS=System.currentTimeMillis();
        while(((System.currentTimeMillis()-lMarkMilliS)<1000)&&(opModeIsActive())) {
            idle();
        }
        lMarkMilliS=System.currentTimeMillis();
        */
