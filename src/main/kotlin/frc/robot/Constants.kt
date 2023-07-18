package frc.robot

import edu.wpi.first.math.util.Units


/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. inside the companion object). Do not put anything functional in this class.
 *
 *
 * It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
class Constants {
    object OperatorConstants {
        const val kDriverControllerPort = 0
    }

    object DriveTrainConstants {
        enum class MotorPosition(val canID: Int, val isLeft: Boolean, val isBack: Boolean) {
            FRONT_LEFT(20, true, false),
            FRONT_RIGHT(21, false, false),
            BACK_LEFT(22, true, false),
            BACK_RIGHT(23, false, true);

            val isRight: Boolean = !isLeft
            val isFront: Boolean = !isBack
        }

        val wheelDiameterMeters = Units.feetToMeters(0.5)

        val trackWidthMeters = Units.feetToMeters(3.0)

        const val maxWheelRadiansPerSecond = 24 // Arbitrary

    }
}
