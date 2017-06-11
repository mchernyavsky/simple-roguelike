package ru.spbau.mit.softwaredesign.roguelike.creatures

import asciiPanel.AsciiPanel

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.spbau.mit.softwaredesign.roguelike.world.World

internal class ZombieTest {
    @Test
    fun constructorTest() {
        val zombie = Zombie((World(90, 32)))
        assertEquals(zombie.glyph, 'z')
        assertEquals(zombie.color, AsciiPanel.green)
        assertEquals(zombie.name, "zombie")
        assertEquals(zombie.maxHealth, 30)
        assertEquals(zombie.visionRadius, 5)
        assertEquals(zombie.originAttack, 7)
        assertEquals(zombie.originDefense, 2)
        assertNull(zombie.armor)
        assertNull(zombie.weapon)
    }

    @Test
    fun updateTest() {
        val zombie = Zombie((World(90, 32)))
        val oldPosition = zombie.position
        zombie.update()
        val newPosition = zombie.position
        val offset = newPosition - oldPosition
        assertTrue(offset.x in -1..1)
        assertTrue(offset.y in -1..1)
    }
}