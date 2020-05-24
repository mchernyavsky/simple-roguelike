package ru.spbau.mit.softwaredesign.roguelike

import asciiPanel.AsciiPanel
import ru.spbau.mit.softwaredesign.roguelike.screens.Screen
import ru.spbau.mit.softwaredesign.roguelike.screens.StartScreen
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JFrame

class ApplicationMain : JFrame(), KeyListener {
    private val terminal: AsciiPanel = AsciiPanel()
    private var screen: Screen? = StartScreen(terminal)

    init {
        add(terminal)
        pack()
        addKeyListener(this)
        repaint()
    }

    override fun repaint() {
        terminal.clear()
        screen?.repaint()
        super.repaint()
    }

    override fun keyPressed(e: KeyEvent) {
        screen = screen?.dispatchKeyEvent(e)
        repaint()
    }

    override fun keyReleased(e: KeyEvent) {}

    override fun keyTyped(e: KeyEvent) {}
}

fun main() {
    val application = ApplicationMain()
    application.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    application.isVisible = true
}
