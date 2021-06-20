package thesis

import com.google.gson.Gson
import thesis.model.mtsp.DSetup
import java.io.File

fun main() {
    val f = File("D:\\Git\\GitHub\\SourceCodes\\Kotlin\\Web\\ThesisServer\\input\\example.json")
    val gson = Gson()
    val setup: DSetup = gson.fromJson(f.readText(), DSetup::class.java)
}