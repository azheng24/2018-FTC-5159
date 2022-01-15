package org.punabots.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

/**
 * This 2018-2019 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine the position of the gold and silver minerals.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
/**
 * This is NOT an opmode.
 *
 * This class is used to define all the specific navigation tasks for the Target Tracking Demo
 * It focuses on setting up and using the Vuforia Library, which is part of the 2016-2017 FTC SDK
 *
 * Once a target is identified, its information is displayed as telemetry data.
 * To approach the target, three motion priorities are created:
 * - Priority #1 Rotate so the robot is pointing at the target (for best target retention).
 * - Priority #2 Drive laterally based on distance from target center-line
 * - Priority #3 Drive forward based on the desired target standoff distance
 *
 */

public class VisionProcessing_5159_v1
{
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";

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
    private static final String VUFORIA_KEY = "ARpyvH3/////AAAAGUEBp0pyAEOGrbwNCST7HOFGFCuzOk+H2t9Rf8v9L32G+TxIVxR/lzc2ZhtdNT0mUUSzGphf7e4P5I+nHks6WF5cG+ql9AMq7G2F3FE2+Iq+/EDRR6LC8cVpMu0DxiPJd3Un9FfEFFtpyr6chK4+UHs6AnlNxyAcOB4zZ7C6QoI7YMr5fgOPWdjPumCFYgX79HS+j5BtFUMSjdlvhX9yN8W9kiYroFvvUvna2b7AukywBxwhY7lOPuHlphsOifYLupzJXKdTzGlC9uPdcYJhk3pYyGyovF03sntlivar54AGo0PL8j8I4ESG0MfRpxclgvzBm7g3SXawgfn8aemgoI0BgdQAM9egixKNJjGPlZlr";


    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
     * Detection engine.
     */
    public TFObjectDetector tfod;

    /**
     * Initialize the Vuforia localization engine.
     */
    public void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    public void initTfod(LinearOpMode linopmode) {
        int tfodMonitorViewId = linopmode.hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", linopmode.hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}

