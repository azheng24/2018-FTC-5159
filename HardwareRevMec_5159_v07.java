package org.punabots.ftc.teamcode;


import android.view.View;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * This is NOT an opmode.
 *
 */


public class HardwareRevMec_5159_v07
{
    // Private Members
    private LinearOpMode opMode;

    public DcMotor  mtrLeftBack      = null;
    public DcMotor  mtrRightBack     = null;
    public DcMotor  mtrLeftFront      = null;
    public DcMotor  mtrRightFront    = null;
    //public DcMotor  mtrIntake      = null;
    public DcMotor  mtrElev     = null;
    public DcMotor mtrPivotL     =null;
    public DcMotor mtrPivotR     =null;
    public DcMotor mtrLift     =null;



    public CRServo crsrvoInL = null;
    public CRServo crsrvoInR = null;


    //public static final double INTAKEPWR =  .75;
    //public static final int ELEVPOS_0 = 0;
    //public static final int ELEVPOS_1 = 1000;
    // static final int ELEVPOS_2 = 3000;
    //public static final int ELEVPOS_3 = 4500;
    //public static final int ELEVPOS_4 = 6000;
/*
    public static final double GRABLOPEN = 0.8;
    public static final double GRABLCLOSE = 0.17;
    public static final double GRABROPEN = 0.2;
    public static final double GRABRCLOSE=.86;
    public static final int GRABLEFT=-1;
    public static final int GRABRIGHT=1;
*/

    public static final double INTAKELFORWARD = -.75;
    public static final double INTAKELREVERSE = .75;
    public static final double INTAKERFORWARD = -.75;
    public static final double INTAKERREVERSE = .75;
    public static final double INTAKERSTOP = 0;
    public static final double INTAKELSTOP = 0;
    public static final int INTAKELEFT=-1;
    public static final int INTAKERIGHT=1;


    public static final double FULLPOWER =.99;
    public static final double SLOWPOWER = .5;
    public static final int FASTFWD = 100;
    public static final int FASTREV = -100;
    public static final int SLOWFWD =  1;
    public static final int SLOWREV = -1;




    //private static final float FINETHRESHHOLD= (float) 10.0;
    //private static final float FINETOLERANCE= (float)2.0;
    //private static final float TURNTOLERANCE= (float) 3.0;

    //public static final int ELEVDOWN = 0;
    //public static final int ELEVMID = 950;

    //Rev Color sensor
    public ColorSensor sensColorMineralL;//, sensColorIntake;
    public DistanceSensor sensProxMineralL; //, sensDistIntake;
    public ColorSensor sensColorMineralR;//, sensColorIntake;
    public DistanceSensor sensProxMineralR; //, sensDistIntake;
    //Rev 2m Sensor used as a regular DistanceSensor.
    public DistanceSensor sensorRangeL;
    public DistanceSensor sensorRangeR;
    // you can also cast this to a Rev2mDistanceSensor if you want to use added
    // methods associated with the Rev2mDistanceSensor class.
    //Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor)sensorRangeL;




    // hsvValues is an array that will hold the hue, saturation, and value information.
    float hsvValuesL[] = {0F, 0F, 0F};
    // values is a reference to the hsvValues array.
    final float valuesL[] = hsvValuesL;
    // sometimes it helps to multiply the raw RGB values with a scale factor
    // to amplify/attentuate the measured values.
    final double SCALE_FACTOR = 255;
    // get a reference to the RelativeLayout so we can change the background
    // color of the Robot Controller app to match the hue detected by the RGB sensor.
    int relativeLayoutId;
    // = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
    View relativeLayout=null;
    //= ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);
    //private static boolean bPreSetRunning;



    //NavX
    IntegratingGyroscope gyro;
    NavxMicroNavigationSensor navxMicro;

    /* Constructor */
    public HardwareRevMec_5159_v07(){

    }


    /* Initialize standard Hardware interfaces */
    public void initHardware(LinearOpMode opMode) {

        // Save reference to Hardware map
          /* local OpMode members. */
        HardwareMap hwMap           =  null;


        // Define and Initialize Motors
        mtrLeftBack        = opMode.hardwareMap.get(DcMotor.class, "mtrLeftBack");
        mtrRightBack       = opMode.hardwareMap.get(DcMotor.class, "mtrRightBack");
        mtrLeftFront        = opMode.hardwareMap.get(DcMotor.class, "mtrLeftFront");
        mtrRightFront       = opMode.hardwareMap.get(DcMotor.class, "mtrRightFront");
        //      = opMode.hardwareMap.get(DcMotor.class, "mtrIntake");
        mtrElev      = opMode.hardwareMap.get(DcMotor.class, "mtrElev");
        mtrPivotL    = opMode.hardwareMap.get(DcMotor.class, "mtrPivotL");
        mtrPivotR    = opMode.hardwareMap.get(DcMotor.class, "mtrPivotR");
        mtrLift     = opMode.hardwareMap.get(DcMotor.class, "mtrLift");
        
        mtrRightBack.setDirection(DcMotor.Direction.FORWARD); // Positive input rotates counter clockwise
        mtrRightFront.setDirection(DcMotor.Direction.FORWARD); // Positive input rotates counter clockwise
        mtrLeftBack.setDirection(DcMotor.Direction.REVERSE);// Positive input rotates counter clockwise
        mtrLeftFront.setDirection(DcMotor.Direction.REVERSE);// Positive input rotates counter clockwise

        mtrElev.setDirection(DcMotor.Direction.REVERSE);
        mtrPivotL.setDirection(DcMotor.Direction.REVERSE);
        mtrPivotR.setDirection(DcMotor.Direction.REVERSE);
        mtrLift.setDirection(DcMotor.Direction.REVERSE);

        // Stop all robot motion by setting each axis value to zero
        mtrLeftBack.setPower(0);
        mtrRightBack.setPower(0);
        mtrLeftFront.setPower(0);
        mtrRightFront.setPower(0);
        //mtrIntake.setPower(0);
        mtrElev.setPower(0);
        mtrPivotL.setPower(0);
        mtrPivotR.setPower(0);
        mtrLift.setPower(0);

        //servos
        crsrvoInL =opMode.hardwareMap.get(CRServo.class, "srvoIntakeLeft");
        crsrvoInR =opMode.hardwareMap.get(CRServo.class, "srvoIntakeRight");
        crsrvoInL.setDirection(CRServo.Direction.FORWARD);
        crsrvoInR.setDirection(CRServo.Direction.REVERSE);

        //sensors
        //get a reference to the color sensor.
        sensColorMineralL = opMode.hardwareMap.get(ColorSensor.class, "sensClrProxMineralL");
        // get a reference to the distance sensor that shares the same name.
        sensProxMineralL = opMode.hardwareMap.get(DistanceSensor.class, "sensClrProxMineralL");
        //get a reference to the color sensor.
        sensColorMineralR = opMode.hardwareMap.get(ColorSensor.class, "sensClrProxMineralR");
        // get a reference to the distance sensor that shares the same name.
        sensProxMineralR = opMode.hardwareMap.get(DistanceSensor.class, "sensClrProxMineralR");


        // you can use Rev 2m as a regular DistanceSensor.
        sensorRangeL = opMode.hardwareMap.get(DistanceSensor.class, "sensDistL");
        sensorRangeR = opMode.hardwareMap.get(DistanceSensor.class, "sensDistR");

        // you can also cast this to a Rev2mDistanceSensor if you want to use added
        // methods associated with the Rev2mDistanceSensor class.
        Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor) sensorRangeL;
        //Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor) sensorRangeR;

        // A timer helps provide feedback while calibration is taking place
        navxMicro = opMode.hardwareMap.get(NavxMicroNavigationSensor.class, "navx");
        gyro = (IntegratingGyroscope)navxMicro;
        gyro = navxMicro;
        //The NavX gyro automatically starts calibrating. This takes a few seconds.
        opMode.telemetry.addData("Status:","Gyro Calibrating...Do Not Move!");
        opMode.telemetry.update();
        // Wait until the gyro calibration is complete
        while (navxMicro.isCalibrating()&&opMode.opModeIsActive())  {
            opMode.idle();
        }

    }



    public void waitforcalibrateNavX (LinearOpMode linopmode) {

        //while (navxMicro.isCalibrating()&&linopmode.opModeIsActive())  {
           //linopmode.idle();
        //}
    }
    String formatRate(float rate) {
        return String.format("%.3f", rate);
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format("%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    float getHeading() {
         Orientation angles = gyro.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return AngleUnit.DEGREES.normalize(AngleUnit.DEGREES.fromUnit(angles.angleUnit,angles.firstAngle));
    }
    public void positionServosTeleop() {

        crsrvoInL.setPower(INTAKELSTOP);
        crsrvoInR.setPower(INTAKERSTOP);
        //srvoGate.setPosition(SERVOGATEDOWN);
        //srvoBallRelease.setPosition(SERVORELEASECLOSE);
        //srvoIntake.setPosition(SERVOINTAKESTOP);
    }

    public void positionServosAuton() {
        crsrvoInL.setPower(INTAKELSTOP);
        crsrvoInR.setPower(INTAKERSTOP);

    }

    void resetMotorsAuton() {

        mtrLeftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrLeftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mtrLeftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrLeftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mtrRightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrRightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        mtrRightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrRightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mtrElev.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrElev.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrElev.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mtrElev.setTargetPosition(0);

        mtrPivotL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrPivotL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrPivotL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mtrPivotL.setTargetPosition(0);


        mtrPivotR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrPivotR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrPivotR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mtrPivotR.setTargetPosition(0);

        mtrLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mtrLift.setTargetPosition(0);
    }

    void resetMotorsTeleop() {

        mtrLeftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mtrLeftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mtrLeftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mtrLeftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mtrRightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mtrRightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mtrRightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mtrRightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //mtrElev.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mtrElev.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //mtrElev.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //mtrElev.setTargetPosition(0);
        mtrElev.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mtrPivotL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrPivotL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrPivotL.setTargetPosition(0);
        mtrPivotL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mtrPivotR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mtrPivotR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtrPivotR.setTargetPosition(0);
        mtrPivotR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //mtrLift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mtrLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //mtrLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //mtrLift.setTargetPosition(0);
        mtrLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    float turnRightHeading (float fTargetHeading, LinearOpMode linopmode,double dTimeOutMS) {
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
        mtrLeftBack.setPower(.5);

        mtrRightBack.setPower(-.5);


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

    //check results... comment out later

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


        //check results... comment out later

        return (getHeading());


    }
    float turnLeftHeading (float fTargetHeading, LinearOpMode linopmode,double dTimeOutMS) {
        //turn left so getting more positive
        double dTimestamp=System.currentTimeMillis();
        float fHeading;
        int nLeftBackEncoderAtExit=0;
        int nRightBackEncoderAtExit=0;
        int nDiff;
        mtrLeftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mtrLeftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mtrRightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        mtrRightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        //turning left, so getting more positive
        //turn left, then see how many degrees turned, then see how much encoder moved
        fHeading = getHeading();


        mtrLeftBack.setPower(-1);
        mtrRightBack.setPower(1);
        mtrLeftFront.setPower(-1);
        mtrRightFront.setPower(1);


        while((fHeading<fTargetHeading)&&((System.currentTimeMillis()-dTimestamp)<dTimeOutMS)
                &&linopmode.opModeIsActive()) {

            fHeading = getHeading();
            linopmode.telemetry.addData("inside left turn:","target:%.2f current:%.2f",
                    fTargetHeading,getHeading());
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
mtrLeftFront.setPower(0);
        mtrRightBack.setPower(0);
        mtrRightFront.setPower(0);

        //check results... comment out later

/*
        //correct
        mtrLeftBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        mtrRightBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        nDiff=(mtrLeftBack.getCurrentPosition()-nLeftBackEncoderAtExit)*7;
        mtrLeftBack.setTargetPosition(mtrLeftBack.getCurrentPosition()+Math.abs(nDiff));

        nDiff=(mtrRightBack.getCurrentPosition()-nRightBackEncoderAtExit)*7;
        mtrRightBack.setTargetPosition(mtrRightBack.getCurrentPosition()-Math.abs(nDiff));

        mtrLeftBack.setPower(1);

        mtrRightBack.setPower(1);

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


        //check results... comment out later
*/
        return (getHeading());


    }
}

