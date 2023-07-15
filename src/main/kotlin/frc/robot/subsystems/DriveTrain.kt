package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode.PercentOutput
import com.ctre.phoenix.motorcontrol.can.TalonFX
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants.DriveTrainConstants.MotorPosition
import frc.robot.Constants.DriveTrainConstants.MotorPosition.*

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

    private val driveMotors = MotorPosition.values().associateWith { TalonFX(it.canID) }

    init {
        // Set left motors inverted
        driveMotors.forEach { (motorPosition, talonFX) -> talonFX.inverted = motorPosition.isLeft }

        // Set front motors to follow back motors
        driveMotors.getValue(FRONT_LEFT).follow(driveMotors.getValue(BACK_LEFT));
        driveMotors.getValue(FRONT_RIGHT).follow(driveMotors.getValue(BACK_RIGHT))
    }

    /** This method will be called once per scheduler run  */
    override fun periodic() {
        driveMotors
            .filter { (motorPosition, _) -> motorPosition.isBack }
            .forEach { (motorPosition, talonFX) ->
            talonFX.set(
                PercentOutput,
                if (motorPosition.isLeft) leftOutput else rightOutput
            )
        }
    }

    /** This method will be called once per scheduler run during simulation  */
    override fun simulationPeriodic() {
    }
}
