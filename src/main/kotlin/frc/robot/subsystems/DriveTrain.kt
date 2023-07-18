package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput
import com.ctre.phoenix.motorcontrol.can.TalonFX
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.wpilibj2.command.CommandBase
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants.DriveTrainConstants
import frc.robot.Constants.DriveTrainConstants.MotorPosition
import frc.robot.Constants.DriveTrainConstants.MotorPosition.*
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/** Creates a new ExampleSubsystem.  */
class DriveTrain : SubsystemBase() {
    var leftOutput = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }

    var rightOutput = 0.0
        set(value) {
            field = value.coerceIn(-1.0, 1.0)
        }

    var pose2d = Pose2d()

    private val driveMotors = MotorPosition.values().associateWith { TalonFX(it.canID) }

    init {
        // Set left motors inverted
        driveMotors.forEach { (motorPosition, talonFX) -> talonFX.inverted = motorPosition.isLeft }

        // Set front motors to follow back motors
        driveMotors.getValue(FRONT_LEFT).follow(driveMotors.getValue(BACK_LEFT));
        driveMotors.getValue(FRONT_RIGHT).follow(driveMotors.getValue(BACK_RIGHT))
    }

    fun setArcadeDrive(leftInput: () -> Double, rightInput: () -> Double): CommandBase {
        return run {
            leftOutput = leftInput()
            rightOutput = rightInput()
        }
    }

    /** This method will be called once per scheduler run  */
    override fun periodic() {
        driveMotors[BACK_LEFT]?.set(PercentOutput, leftOutput)
        driveMotors[BACK_RIGHT]?.set(PercentOutput, rightOutput)

        val leftDistance =
            leftOutput * DriveTrainConstants.maxWheelRadiansPerSecond * DriveTrainConstants.wheelDiameterMeters
        val rightDistance =
            rightOutput * DriveTrainConstants.maxWheelRadiansPerSecond * DriveTrainConstants.wheelDiameterMeters

        if (abs(leftDistance - rightDistance) < 1.0e-6) { // basically going straight
            pose2d = Pose2d(
                pose2d.translation + Translation2d(
                    leftDistance * cos(pose2d.rotation.radians),
                    rightDistance * sin(pose2d.rotation.radians)
                ), pose2d.rotation
            )
        } else {
            val currentHeading = pose2d.rotation.radians
            val r =
                DriveTrainConstants.trackWidthMeters * (leftDistance + rightDistance) / (2 * (rightDistance - leftDistance))
            val wd = (rightDistance - leftDistance) / DriveTrainConstants.trackWidthMeters
            pose2d = Pose2d(
                pose2d.translation + Translation2d(
                    r * sin(wd + currentHeading) - r * sin(currentHeading),
                    r * cos(wd + currentHeading) + r * cos(currentHeading)
                ), Rotation2d(pose2d.rotation.radians + wd)
            )
        }
    }

    /** This method will be called once per scheduler run during simulation  */
    override fun simulationPeriodic() {

    }
}
