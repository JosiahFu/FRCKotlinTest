package frc.robot.simulation

import edu.wpi.first.wpilibj.smartdashboard.Field2d
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.robot.subsystems.DriveTrain

class FieldSim (private val driveTrain: DriveTrain) {
    private val field2d = Field2d()

    fun initSim() {
    }

    fun simulationPeriodic() {
        field2d.robotPose = driveTrain.pose2d
        SmartDashboard.putData("Field2d", field2d)
    }
}