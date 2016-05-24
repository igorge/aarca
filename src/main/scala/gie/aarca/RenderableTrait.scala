package gie.aarca

import com.badlogic.gdx.graphics.g2d.SpriteBatch

trait RenderableTrait {

    def update(): Unit // update before render
    def render(batch: SpriteBatch): Unit
}