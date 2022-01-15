package org.punabots.ftc.teamcode;


import android.view.View;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * This is NOT an opmode.
 */

public class HardwareTestBed_5159_v00 {

    // Private Members
    private LinearOpMode opMode;

    public DcMotor mtrLeftBack = null;
    public DcMotor mtrRightBack = null;
    public DcMotor mtrIntake = null;
    public DcMotor mtrElev = null;

    public IntegratingGyroscope gyro;
    public NavxMicroNavigationSensor navxMicro;

    public Servo srvoLoader = null;
    //public static final double SERVOLOADERTRANSPORT = 0.99;
    //public static final double SERVOLOADERLOAD = -0.99;//.5 is flat
    //public static final double SERVOLOADERDUMP = 0.;
    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   50;     // period of each cycle
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.0;     // Minimum rotational position

    //double  position = (MAX_POS - MIN_POS) / 2; // Start at halfway position
    double  position = (0.1); // Start at halfway position
    //boolean rampUp = true;

    double angles = 0.0;  // start pointing to 53 degree

    //Rev Color sensor
    //public ColorSensor sensColorBall;
    //public DistanceSensor sensDistBall;

    // The following values are helpful for the color sensor
    // hsvValues is an array that will hold the hue, saturation, and value information.

    float hsvValues[] = {0F, 0F, 0F};
    // values is a reference to the hsvValues array.
    final float values[] = hsvValues;
    // sometimes it helps to multiply the raw RGB values with a scale factor
    // to amplify/attentuate the measured values.
    final double SCALE_FACTOR = 255;
    // get a reference to the RelativeLayout so we can change the background
    // color of the Robot Controller app to match the hue detected by the RGB sensor.
    int relativeLayoutId;
    // = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
    View relativeLayout = null;
    //= ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);
    //private static boolean bPreSetRunning;

    //The following values are helpful for the IMU
    //NavX
    //IntegratingGyroscope gyro;
    //NavxMicroNavigationSensor navxMicro;

    private static final float TURNTOLERANCE = (float) 3.0;

    /* Constructor */
    public HardwareTestBed_5159_v00() {

    }


    /* Initialize standard Hardware interfaces */
    public void initHardware(LinearOpMode opMode) {

        // Save reference to Hardware map
        /* local OpMode members. */
        HardwareMap hwMap = null;


        // Define and Initialize Motors
        mtrLeftBack = opMode.hardwareMap.get(DcMotor.class, "mtrLeftBack");
        mtrRightBack = opMode.hardwareMap.get(DcMotor.class, "mtrRightBack");
        mtrIntake = opMode.hardwareMap.get(DcMotor.class, "mtrIntake");
        mtrElev = opMode.hardwareMap.get(DcMotor.class, "mtrElev");

        mtrRightBack.setDirection(DcMotor.Direction.FORWARD); // Positive input rotates counter clockwise
        mtrLeftBack.setDirection(DcMotor.Direction.REVERSE);// Positive input rotates counter clockwise
        mtrIntake.setDirection(DcMotor.Direction.FORWARD);// Positive input rotates counter clockwise
        mtrElev.setDirection(DcMotor.Direction.REVERSE);// Positive input rotates counter clockwise


        //use RUN_USING_ENCODERS because encoders are installed.

        mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mtrIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mtrIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        mtrElev.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrElev.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        // Stop all robot motion by setting each axis value to zero
        mtrLeftBack.setPower(0);
        mtrRightBack.setPower(0);
        mtrIntake.setPower(0);
        mtrElev.setPower(0);

        //servos
        srvoLoader=opMode.hardwareMap.get(Servo.class, "srvoLoader");

        // Get a reference to a Modern Robotics GyroSensor object. We use several interfaces
        // on this object to illustrate which interfaces support which functionality.
        navxMicro = opMode.hardwareMap.get(NavxMicroNavigationSensor.class, "navx");
        gyro = (IntegratingGyroscope)navxMicro;


    }


    /*public void waitforcalibrateNavX(LinearOpMode linopmode) {

        while (navxMicro.isCalibrating()&&linopmode.opModeIsActive())  {
           linopmode.idle();
        }
    }
    */


    String formatRate(float rate) {
        return String.format("%.3f", rate);
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees) {
        return String.format("%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    float getHeading() {
        //Orientation angles = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        //return AngleUnit.DEGREES.normalize(AngleUnit.DEGREES.fromUnit(angles.angleUnit,angles.firstAngle));
        return 0;
        }

    public void positionServosTeleop() {
        srvoLoader.setPosition(position);

    }

    public void positionServosAuton() {
        srvoLoader.setPosition(position);

    }


    void resetMotorsAuton() {

        mtrLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mtrRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mtrIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mtrIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        mtrElev.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrElev.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrElev.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
    void resetMotorsTeleop() {

        mtrLeftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mtrRightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mtrIntake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mtrIntake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        mtrElev.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrElev.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrElev.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
    void goForward (int nAddlEncoderCount, double dPwr,LinearOpMode linopmode,double dTimeOutMS){
        mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrLeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        mtrRightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mtrLeftBack.setTargetPosition(mtrLeftBack.getCurrentPosition()+nAddlEncoderCount);
        mtrRightBack.setTargetPosition(mtrRightBack.getCurrentPosition()+nAddlEncoderCount);
        mtrLeftBack.setPower(dPwr);
        mtrRightBack.setPower(dPwr);
        double dTimestamp = System.currentTimeMillis();
        while((mtrRightBack.isBusy()||mtrLeftBack.isBusy())&&((System.currentTimeMillis()-dTimestamp)<dTimeOutMS)&& linopmode.opModeIsActive()) {
            linopmode.idle();
        }
        mtrRightBack.setPower(0);
        mtrLeftBack.setPower(0);
        mtrRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        double dTimestampEnd=System.currentTimeMillis();
        /*
        while(((System.currentTimeMillis()-dTimestampEnd)<500)&&linopmode.opModeIsActive()) {
            linopmode.idle();
        }*/
    }

    void goBackward (int nEncoderCount, double dPwr, LinearOpMode linopmode,double dTimeoutMS){
        mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrLeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        mtrRightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mtrLeftBack.setTargetPosition(mtrLeftBack.getCurrentPosition()-nEncoderCount);
        mtrRightBack.setTargetPosition(mtrRightBack.getCurrentPosition()-nEncoderCount);
        mtrLeftBack.setPower(-dPwr);
        mtrRightBack.setPower(-dPwr);
        double dTimeStamp=System.currentTimeMillis();
        while((mtrRightBack.isBusy()||mtrLeftBack.isBusy())&&((System.currentTimeMillis()-dTimeStamp)<dTimeoutMS)&&linopmode.opModeIsActive()) {
            linopmode.idle();
        }
        mtrRightBack.setPower(0);
        mtrLeftBack.setPower(0);
        mtrRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        double dTimestampEnd=System.currentTimeMillis();
        while(((System.currentTimeMillis()-dTimestampEnd)<500)&&linopmode.opModeIsActive()) {
            linopmode.idle();
        }
    }

    float turnRightHeadingPwr (float fTargetHeading, LinearOpMode linopmode,double dTimeOutMS,double dPwr) {
        //turn right so get more negative
        double dTimestamp=System.currentTimeMillis();
        float fHeading;
        int nLeftBackEncoderAtExit=0;
        int  nRightBackEncoderAtExit=0;
        int nDiff;

        resetMotorsAuton();

        //turning right, so getting more negative
        //turn right, then see how many degrees turned, then see how much encoder moved
        fHeading = getHeading();
        if(Math.abs(fTargetHeading-fHeading)<=TURNTOLERANCE) return fHeading;
        mtrLeftBack.setPower(dPwr);

        mtrRightBack.setPower(-dPwr);



        while((fHeading>=fTargetHeading)&&((System.currentTimeMillis()-dTimestamp)<dTimeOutMS)&&linopmode.opModeIsActive()) {

            fHeading = getHeading();
            linopmode.telemetry.addData("inside right turn:","target:%.2f current:%.2f", fTargetHeading,getHeading());
            linopmode.telemetry.addData("   ","lefttbackpos:%d",
                    mtrLeftBack.getCurrentPosition());
            linopmode.telemetry.addData("   ","leftbackpwr:%.2f",
                    mtrLeftBack.getPower());

            linopmode.telemetry.update();
            nLeftBackEncoderAtExit=mtrLeftBack.getCurrentPosition();

            nRightBackEncoderAtExit=mtrRightBack.getCurrentPosition();

            //linopmode.idle();
        }
        mtrLeftBack.setPower(0);

        mtrRightBack.setPower(0);

        //correct
        mtrLeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        mtrRightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        nDiff=(mtrLeftBack.getCurrentPosition()-nLeftBackEncoderAtExit)*7;//this will be positive
        mtrLeftBack.setTargetPosition(mtrLeftBack.getCurrentPosition()-Math.abs(nDiff));

        nDiff=(mtrRightBack.getCurrentPosition()-nRightBackEncoderAtExit)*7;//this will be negative
        mtrRightBack.setTargetPosition(mtrRightBack.getCurrentPosition()+Math.abs(nDiff));

        mtrLeftBack.setPower(.67);

        mtrRightBack.setPower(.67);

        while((mtrLeftBack.isBusy()||mtrRightBack.isBusy())
                &&((System.currentTimeMillis()-dTimestamp)<2500)&& linopmode.opModeIsActive()) {
            linopmode.telemetry.addData("correcting right turn move:","target:%.2f current:%.2f", fTargetHeading,getHeading());

            linopmode.telemetry.addData("   ","leftback now: %d leftback exit:%d left back target:",mtrLeftBack.getCurrentPosition(),
                    nLeftBackEncoderAtExit,mtrLeftBack.getTargetPosition());
            linopmode.telemetry.addData("   ","rightback now: %d rightback exit:%d right back target:",mtrRightBack.getCurrentPosition(),
                    nRightBackEncoderAtExit,mtrRightBack.getTargetPosition());
            linopmode.telemetry.update();
        }
        mtrLeftBack.setPower(0);

        mtrRightBack.setPower(0);

        return (getHeading());


    }



    float turnLeftHeadingPwr (float fTargetHeading, LinearOpMode linopmode,double dTimeOutMS,double dPwr) {
        //turn right so get more negative
        double dTimestamp=System.currentTimeMillis();
        float fHeading;
        int nLeftBackEncoderAtExit=0;
        int nRightBackEncoderAtExit=0;
        int nDiff;

        resetMotorsAuton();

        //turning left, so getting more positive
        //turn left, then see how many degrees turned, then see how much encoder moved
        fHeading = getHeading();
        if(Math.abs(fTargetHeading-fHeading)<=TURNTOLERANCE) return fHeading;
        mtrLeftBack.setPower(-dPwr);

        mtrRightBack.setPower(dPwr);


        while((fHeading<fTargetHeading)&&((System.currentTimeMillis()-dTimestamp)<dTimeOutMS)&&linopmode.opModeIsActive()) {

            fHeading = getHeading();
            linopmode.telemetry.addData("inside left turn:","target:%.2f current:%.2f", fTargetHeading,getHeading());
            linopmode.telemetry.addData("   ","lefttbackpos:%d",
                    mtrLeftBack.getCurrentPosition());
            linopmode.telemetry.addData("   ","leftbackpwr:%.2f",
                    mtrLeftBack.getPower());

            linopmode.telemetry.update();
            nLeftBackEncoderAtExit=mtrLeftBack.getCurrentPosition();

            nRightBackEncoderAtExit=mtrRightBack.getCurrentPosition();

            //linopmode.idle();
        }
        mtrLeftBack.setPower(0);

        mtrRightBack.setPower(0);


        //correct
        mtrLeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        mtrRightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        nDiff=(mtrLeftBack.getCurrentPosition()-nLeftBackEncoderAtExit)*7;
        mtrLeftBack.setTargetPosition(mtrLeftBack.getCurrentPosition()+Math.abs(nDiff));

        nDiff=(mtrRightBack.getCurrentPosition()-nRightBackEncoderAtExit)*7;
        mtrRightBack.setTargetPosition(mtrRightBack.getCurrentPosition()-Math.abs(nDiff));

        mtrLeftBack.setPower(.67);

        mtrRightBack.setPower(.67);

        while((mtrLeftBack.isBusy()||mtrRightBack.isBusy())
                &&((System.currentTimeMillis()-dTimestamp)<2500)&& linopmode.opModeIsActive()) {
            linopmode.telemetry.addData("correcting right turn move:","target:%.2f current:%.2f", fTargetHeading,getHeading());

            linopmode.telemetry.addData("   ","leftback now: %d leftback exit:%d left back target:",mtrLeftBack.getCurrentPosition(),
                    nLeftBackEncoderAtExit,mtrLeftBack.getTargetPosition());

            linopmode.telemetry.addData("   ","rightback now: %d rightback exit:%d right back target:",mtrRightBack.getCurrentPosition(),
                    nRightBackEncoderAtExit,mtrRightBack.getTargetPosition());
            linopmode.telemetry.update();
        }
        mtrLeftBack.setPower(0);

        mtrRightBack.setPower(0);



        return (getHeading());


    }

}
