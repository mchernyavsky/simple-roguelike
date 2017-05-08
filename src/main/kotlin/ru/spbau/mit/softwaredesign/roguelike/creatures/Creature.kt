package ru.spbau.mit.softwaredesign.roguelike.creatures

import ru.spbau.mit.softwaredesign.roguelike.items.Armor
import ru.spbau.mit.softwaredesign.roguelike.items.Food
import ru.spbau.mit.softwaredesign.roguelike.items.Weapon
import ru.spbau.mit.softwaredesign.roguelike.world.GameObject
import ru.spbau.mit.softwaredesign.roguelike.world.Point
import ru.spbau.mit.softwaredesign.roguelike.world.World

import java.awt.Color

abstract class Creature(
        val world: World,
        glyph: Char, color: Color,
        val name: String,
        val maxHealth: Int,
        val visionRadius: Int,
        val originAttack: Int, val originDefense: Int,
        armor: Armor? = null, weapon: Weapon? = null
) : GameObject(glyph, color) {
    var health: Int = maxHealth

    var armor: Armor? = armor
        protected set

    var weapon: Weapon? = weapon
        protected set

    val attack: Int
        get() = originAttack + (weapon?.attack ?: 0)

    val defense: Int
        get() = originDefense + (armor?.defense ?: 0)

    abstract fun update()

    abstract fun notify(message: String)

    fun notifyAll(message: String) {
        for (x in -visionRadius until visionRadius) {
            for (y in -visionRadius until visionRadius) {
                if (x * x + y * y > visionRadius * visionRadius) {
                    continue
                }

                val other = world.getCreature(position + Point(x, y)) ?: continue
                if (other === this) {
                    other.notify("You $message.")
                } else if (other.canSee(position)) {
                    other.notify(String.format("The $name ${makeSecondPersonView(message)}."))
                }
            }
        }
    }

    abstract fun canSee(otherPosition: Point): Boolean

    abstract fun move(newPosition: Point)

    fun moveBy(offset: Point) = move(position + offset)

    open fun attack(other: Creature) {
        val amount = (Math.max(0, attack - other.defense) * Math.random()).toInt() + 1
        other.modifyHealth(-amount)
        notifyAll("attack the ${other.name} for $amount attack")
    }

    open fun dig(position: Point) {
        world.dig(position)
        notifyAll("dig")
    }

    fun modifyHealth(amount: Int) {
        health += amount
        if (health > maxHealth) {
            health = maxHealth
        } else if (health < 1) {
            world.removeCreature(this)
            leaveCorpse()
            notifyAll("die")
        }
    }

    private fun leaveCorpse() {
        val corpse = Food('%', color, "$name's corpse", maxHealth / 2)
        world.dropItem(position, corpse)
    }

    private fun makeSecondPersonView(text: String): String {
        val words = text.split(' ').toTypedArray()
        words[0] += "s"
        return words.joinToString(" ")
    }
}