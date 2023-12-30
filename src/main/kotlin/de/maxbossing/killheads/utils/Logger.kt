package de.maxbossing.killheads.utils

import de.maxbossing.killheads.LOGGER
import de.maxbossing.killheads.data.ConfigController

fun info(vararg msg: String) = msg.forEach { LOGGER.info(it) }

fun warn(vararg msg: String) = msg.forEach { LOGGER.warning(it) }

fun err(vararg msg: String) = msg.forEach { LOGGER.severe(it) }

fun debug(vararg msg: String) = msg.forEach { if (ConfigController.CONFIG.debug) LOGGER.info("[DEBUG] $it") }