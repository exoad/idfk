package net.exoad.idfk.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.ashley.mapperFor
import net.exoad.idfk.Shared
import net.exoad.idfk.ecs.component.PlayerComponent
import net.exoad.idfk.ecs.component.PositionComponent
import net.exoad.idfk.ecs.component.SizeComponent

class CameraSystem(private val camera: OrthographicCamera, private val batch: SpriteBatch) : EntitySystem() {
    private val positionMapper = mapperFor<PositionComponent>()
    private val sizeMapper = mapperFor<SizeComponent>()

    override fun update(deltaTime: Float) {
        val players =
            engine.getEntitiesFor(allOf(PlayerComponent::class, PositionComponent::class, SizeComponent::class).get())
        if (players.size() > 0) {
            val p = players.first()
            val pos = p[positionMapper]!!
            val size = p[sizeMapper]!!
            val centerX = pos.x + size.width / 2f
            val centerY = pos.y + size.height / 2f
            camera.position.set(centerX / Shared.VISUAL_SCALE, centerY / Shared.VISUAL_SCALE, 0f)
        }
        camera.update()
        batch.projectionMatrix = camera.combined
    }
}
