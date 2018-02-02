package org.ice1000.julia.lang.module

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.projectRoots.*
import com.intellij.openapi.projectRoots.ui.ProjectJdksEditor
import com.intellij.openapi.util.SystemInfo
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.ComboboxWithBrowseButton
import org.ice1000.julia.lang.*
import org.ice1000.julia.lang.editing.JULIA_BIG_ICON
import org.jdom.Element
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import javax.swing.DefaultComboBoxModel
import javax.swing.JList

val defaultExePath by lazy {
	val existPath = PropertiesComponent.getInstance().getValue(JULIA_SDK_HOME_PATH_ID).orEmpty()
	when {
		validateJuliaSDK(existPath) -> existPath
		SystemInfo.isWindows -> findPathWindows() ?: "C:\\Program Files"
		SystemInfo.isMac -> findPathMac()
		else -> findPathLinux() ?: "/usr/share/julia"
	}
}

fun findPathMac(): String {
	val appPath = Paths.get(MAC_APPLICATIONS)
	val result = Files.list(appPath).collect(Collectors.toList()).firstOrNull { application ->
		application.toString().contains("julia", true)
	} ?: appPath
	val folderAfterPath = "/Contents/Resources/julia/bin/julia"
	return result.toAbsolutePath().toString() + folderAfterPath
}

fun findPathWindows() = executeCommandToFindPath("where julia")
private fun findPathLinux() = executeCommandToFindPath("whereis julia")

fun SdkAdditionalData?.toJuliaSdkData() = this as? JuliaSdkData

open class JuliaSdkData(
	var tryEvaluateTimeLimit: Long = 2500L,
	var tryEvaluateTextLimit: Int = 320,
	var importPath: String) : SdkAdditionalData {
	override fun clone() = JuliaSdkData(tryEvaluateTimeLimit, tryEvaluateTextLimit, "")
}

fun versionOf(exePath: String, timeLimit: Long = 500L) =
	executeJulia(exePath, null, timeLimit, "--version")
		.first
		.firstOrNull { it.startsWith("julia version", true) }
		?.dropWhile { it.isLetter() or it.isWhitespace() }
		?: JuliaBundle.message("julia.modules.sdk.unknown-version")

fun importPathOf(exePath: String, timeLimit: Long = 500L) =
	executeJulia(exePath, null, timeLimit, "--print", "\"Pkg.dir()\"")
		.first
		.firstOrNull { Files.isDirectory(Paths.get(it)) }
		?: Paths.get(exePath).parent.parent.toString()

fun validateJuliaSDK(exePath: String) = versionOf(exePath) != JuliaBundle.message("julia.modules.sdk.unknown-version")
