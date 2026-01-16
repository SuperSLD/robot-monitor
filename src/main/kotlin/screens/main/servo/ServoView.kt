package screens.main.servo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import common.components.MCard
import domain.models.ServoMotor
import theme.MonitorText
import kotlin.math.min

@Composable
fun ServoView(
    servoMotor: ServoMotor,
    modifier: Modifier = Modifier,
    canMove: Boolean = false,
    onAngleChange: (String, Double) -> Unit,
) {

    /**
     * Полная высота картинки 719
     * Высота базы 659
     * Высота головы 514
     *
     * Ширина 228
     */
    val servoBasePainter = painterResource("drawables/servo_base.svg")
    val baseSize = servoBasePainter.intrinsicSize
    val servoHeadPainter = painterResource("drawables/servo_head.svg")
    val headSize = servoHeadPainter.intrinsicSize

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MCard(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(servoMotor.humanName, style = MonitorText.Bold.Sp18.White.style())
                Text("Number #${servoMotor.name}", style = MonitorText.Bold.Sp16.Gray.style())
                Canvas(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    val scale = min(
                        size.width / baseSize.width,
                        size.height / baseSize.height
                    )

                    val baseScaledWidth = baseSize.width * scale
                    val baseSdHeight = baseSize.height * scale

                    val headScaledWidth = headSize.width * scale
                    val headScaledHeight = headSize.height * scale

                    val dx = (size.width - baseScaledWidth) / 2f
                    val dy = (size.height - baseSdHeight) / 2f

                    translate(left = dx, top = dy) {
                        with(servoBasePainter) {
                            draw(size = Size(baseScaledWidth, baseSdHeight))
                        }
                    }

                    val headPivot = Offset(
                        x = dx + headScaledWidth / 2f,
                        y = dy + headScaledHeight / 2f
                    )

                    rotate(
                        degrees = servoMotor.currentAngle.toFloat() - 90f,
                        pivot = headPivot
                    ) {
                        translate(left = dx, top = 0f) {
                            with(servoHeadPainter) {
                                draw(size = Size(headScaledWidth, headScaledHeight))
                            }
                        }
                    }
                }
                Text("${servoMotor.currentAngle}°", style = MonitorText.Bold.Sp32.White.style())
            }
        }

        if (canMove) {
            MCard(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(servoMotor.humanName, style = MonitorText.Bold.Sp18.White.style())
                    Text("Change angle: ${servoMotor.currentAngle}°", style = MonitorText.Bold.Sp16.Gray.style())
                    Slider(
                        value = toNormal(servoMotor.currentAngle),
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { onAngleChange(servoMotor.name, fromNormal(it)) }
                    )
                }
            }
        }
    }
}

private fun toNormal(angle: Double) = (angle / 180.0).toFloat()
private fun fromNormal(angle: Float) = (angle * 180.0).toDouble()