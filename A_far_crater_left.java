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

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

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

@Autonomous(name="far_from_crater_left", group="TestBed")
//@Disabled
public class A_far_crater_left extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareTestBed_5159_v00 robot = new HardwareTestBed_5159_v00();
    Robot_Navigation_5159_v3 nav = new Robot_Navigation_5159_v3();  // Use Image Tracking library
    private ElapsedTime runtime = new ElapsedTime();


    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.5;
    static final double TURN_SPEED = 0.5;

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";


    // gyro
    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY = "AUdC2JX/////AAABmYzC4fOZtkR+uSbgRJxi+YsAgdFtHf7LVsnX/rlPhcIGzmj0TxqPs7HQr1byHuUlvB5RzEbxHUO2L5/+e9Qzyiz/4MTcaNAUmZDOpcnxgk2YParx+gRQAVfnHooudH1rQMvnAzzUH5LIpz0LIp350qMTsGHfCtV4xyQXs6BMvLNgIWEuGm73fb0Nl1F7l8cH6v8nVavWY9Vov+Y8CYYx3ewBOt/7YyfsEcdXLFdmZSkzAw3vbsoe4SApVCCT9MW3aQmZaiARqKEvt4S8JDJa+vB2ihyTPPb16+kgwgc2VqrJ5C607IyZdz3iUqPfeBY2EnS7HYVC00ao9ltjbKAbC0F9mxQ0lXRGTRke5c12IC3n";

    //private static final int pos_middle = 1;
    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() throws InterruptedException {
        long lMarkMilliS = System.currentTimeMillis();
        RelicRecoveryVuMark vuMark = null;
        boolean bTimeOut, bExit, bDistValid;
        String strColor = "none";
        float fRedPct, fBluePct;

        int pos = -1;

        // Read dimensionalized data from the gyro. This gyro can report angular velocities
        // about all three axes. Additionally, it internally integrates the Z axis to
        // be able to report an absolute angular Z orientation.
        Orientation cur_angles;

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
        telemetry.addData("Path0", "Starting at %7d :%7d",
                robot.mtrLeftBack.getCurrentPosition(),
                robot.mtrRightBack.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        robot.srvoLoader.setPosition(0.1);

        // A timer helps provide feedback while calibration is taking place
        ElapsedTime timer = new ElapsedTime();

        // If you're only interested int the IntegratingGyroscope interface, the following will suffice.
        // gyro = hardwareMap.get(IntegratingGyroscope.class, "navx");

        // The gyro automatically starts calibrating. This takes a few seconds.
        telemetry.log().add("Gyro Calibrating. Do Not Move!");

        // Wait until the gyro calibration is complete
        timer.reset();
        while (robot.navxMicro.isCalibrating()) {
            telemetry.addData("calibrating", "%s", Math.round(timer.seconds()) % 2 == 0 ? "|.." : "..|");
            telemetry.update();
            Thread.sleep(50);
        }
        telemetry.log().clear();
        telemetry.log().add("Gyro Calibrated.");
        telemetry.clear();
        telemetry.update();


        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        //move robot down
        // comment the following to remove elevator
        elevator(7700, 50);

        /*
        telemetry.addData("heading2", "%.1f", getGyro());
        telemetry.update();
        sleep(3000);
        encoderTurn(10);
        telemetry.addData("heading3", "%.1f", getGyro());
        telemetry.update();
        sleep(3000);
        encoderTurn(40);
        telemetry.addData("heading4", "%.1f", getGyro());
        telemetry.update();
        sleep(3000);
        encoderTurn(100);
        telemetry.addData("heading5", "%.1f", getGyro());
        telemetry.update();
        sleep(3000);
        encoderTurn(-170);
        telemetry.addData("heading6", "%.1f", getGyro());
        telemetry.update();
        sleep(3000);

        //encoderDrive("turn", 1.0, -1000, 1000, 20.0);
        encoderTurn(9.9);
        telemetry.addData("heading7", "%.1f", getGyro());
        telemetry.update();
        sleep(10000);
        if (true) return;
        */

        //moving out from hook
        encoderDrive("away hook", 0.5, 300, 300, 20.0);  // S1: Forward 47 Inches with 5 Sec timeout
        //turning towards balls
        //encoderDrive("turn to ball", 0.5, -750, 750, 20.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
        encoderTurn(86.5);

        telemetry.addData("heading1", "%.1f", getGyro());
        telemetry.update();
        sleep(1);


        pos = find_mineral(7);  // wait for 30s

        //wait
        telemetry.addData("Gold Mineral Position", pos);

        telemetry.update();

        if (pos == -1) {
            pos = 1;
        }


        // testing, force to a position
        //pos = 1;


        //if sense left ball...
        if (pos == 0) { // left
            //turn towards left most ball
            //encoderDrive("turn", 0.5, -150, 150, 20.0);
            encoderTurn(115.0);
            //go forward to hit gold off completely
            encoderDrive("forward", 0.5, 3400, 3400, 20.0);
            //turn to face depot
            //encoderDrive("turn", 0.5, 550, -550, 20.0);
            encoderTurn(40.0);
            //go forward into depot
            encoderDrive("forward", 0.5, 2300, 2300, 20.0);
            //drop team marker
            robot.srvoLoader.setPosition(0.63);
            //wait
            sleep(500);
            //encoderDrive("turn",0.5, 1300, -1300, 20.0);
            //encoderDrive("forward", 0.5, 10000, 10000, 20.0);
            //if sense center ball or no ball...
            encoderTurn(-45.0);
            encoderDrive("forward", 1.0, 8000, 8000, 20.0);
        } else if (pos == 1) { // center
            //encoderDrive("turn", 0.5, 150, -150, 20.0);
            //move forward to hit gold off completely
            encoderDrive("forward", 0.5, 2800, 2800, 20.0);
            //turn to face depot
//            encoderDrive("turn", 0.5, 150, -150, 20.0);
            encoderTurn(60.0);
            //go forward into depot
            encoderDrive("forward", 0.5, 2400, 2400, 20.0);
            //drop team marker
            robot.srvoLoader.setPosition(0.63);
            sleep(500);
            //encoderDrive( "turn", 0.5,1650,  -1650, 20.0);
            //encoderDrive( "forward", 0.5,11200,   11200, 20.0);
            //if sense right ball...
            encoderTurn(-45.0);
            encoderDrive("forward", 1.0, 8000, 8000, 20.0);
        } else if (pos == 2) { // right
            //turn towards right most ball
//            encoderDrive("turn", 0.5, 400, -400, 20.0);
            encoderTurn(60.0);
            //go forward to hit gold off completely
            encoderDrive("forward", 0.5, 3200, 3200, 20.0);
            //turn to face depot
//            encoderDrive("turn", 0.5, -400, 400, 20.0);
            encoderTurn(100.0);
            //go forward into depot
            encoderDrive("forward", 0.5, 2300, 2300, 20.0);
            //drop team marker
            robot.srvoLoader.setPosition(0.63);
            sleep(500);
            //encoderDrive("turn", 0.5, 1800, -1800, 20.0);
            //encoderDrive("forward", 0.5, 10000, 10000, 20.0);
            encoderTurn(-45.0);
            encoderDrive("forward", 1.0, 8000, 8000, 20.0);
        } else {  // never here
            encoderDrive("backwards", 0.5, -100, -100, 20.0);
        }


        /*
        //hitting balls and go to depot
        encoderDrive( 0.5,2800,  2800, 20.0);
        //turning towards depot
        encoderDrive(0.5, 100, -100, 20.0);
        encoderDrive(0.5,2800,2800,20.0);
        //drop team marker
        robot.srvoLoader.setPosition(0.63);
        //wait
        sleep(500);
        // turn to face crater
        encoderDrive( 0.5,-585,  585, 20.0);
        // drive towards crater
        encoderDrive( 0.5,-11200,   -11200, 20.0);

*/

        //robot.leftClaw.setPosition(1.0);            // S4: Stop and close the claw.
        //robot.rightClaw.setPosition(0.0);
        //sleep(1000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

    public void encoderTurn(double desired_angle) {
        double factor = 10;
        double turn = desired_angle - getGyro();
        do {
            if (turn > 180) {
                turn = 360 - turn;
            } else if (turn < -180) {
                turn = 360 + turn;
            }
            encoderDrive("turning", 0.5, (int) (-1.0 * factor * turn), (int) (factor * turn), 2.0);
            turn = desired_angle - getGyro();
        } while (turn > 5.0 || turn < -5.0);

    }

    public double getGyro() {
        Orientation angles;

        angles = robot.gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return AngleUnit.DEGREES.normalize(AngleUnit.DEGREES.fromUnit(angles.angleUnit, angles.firstAngle));
    }
    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(String msg, double speed,
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
        while(((System.currentTimeMillis() - lMarkMilliS) < timeout*1000) && opModeIsActive() && (robot.mtrLeftBack.isBusy() && robot.mtrRightBack.isBusy())) {
            telemetry.addData(msg + " M pos:", "%d, %d", robot.mtrLeftBack.getCurrentPosition(), robot.mtrRightBack.getCurrentPosition());
            telemetry.update();
        }
        robot.mtrLeftBack.setPower(0);
        robot.mtrRightBack.setPower(0);

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

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }
    String formatDegrees(double degrees){
        return String.format("%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    private int find_mineral(int timeout) {

        int pos = -1;
        initVuforia();


        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }

        /* Activate Tensor Flow Object Detection. */
        if (tfod != null) {
            tfod.activate();
        }

        long lMarkMilliS=System.currentTimeMillis();
        while(((System.currentTimeMillis() - lMarkMilliS) < timeout*1000) && opModeIsActive()) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    //telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if (updatedRecognitions.size() >= 2) {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;
                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                goldMineralX = (int) recognition.getLeft();
                            } else if (silverMineral1X == -1) {
                                silverMineral1X = (int) recognition.getLeft();
                            } else {
                                silverMineral2X = (int) recognition.getLeft();
                            }
                        }
                        telemetry.addData("pos:", "%d, %d, %d, %d",updatedRecognitions.size(), goldMineralX, silverMineral1X, silverMineral2X);

                        if (goldMineralX != -1 || silverMineral1X != -1 || silverMineral2X != -1) {
                            if (silverMineral2X != -1) {
                                pos = 2; //"Right";
                            } else if (goldMineralX < silverMineral1X) {
                                pos = 0; // "Left";
                            } else {
                                pos = 1; //"Center";
                            }
                        }
                        telemetry.addData("time", (System.currentTimeMillis() - lMarkMilliS)/1000);
                        telemetry.addData("Gold Mineral Position", pos);
                    }
                    telemetry.update();
                }
            }
        }
        return pos;
    }
}
