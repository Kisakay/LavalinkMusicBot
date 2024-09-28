import com.fasterxml.jackson.module.kotlin.*
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

data class JsonDatabase(val data: MutableMap<String, Any> = mutableMapOf())

class JsonDB(private val filePath: String) {
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
    private var db: JsonDatabase

    init {
        val file = File(filePath)
        db = if (file.exists()) {
            objectMapper.readValue(file, JsonDatabase::class.java)
        } else {
            JsonDatabase()
        }
    }

    fun set(key: String, value: Any) {
        val keys = key.split(".")
        var currentLevel = db.data

        for (i in 0 until keys.size - 1) {
            val subKey = keys[i]
            currentLevel = (currentLevel[subKey] as? MutableMap<String, Any>) ?: mutableMapOf<String, Any>().also {
                currentLevel[subKey] = it
            }
        }

        currentLevel[keys.last()] = value
        save()
    }

    fun get(key: String): Any? {
        val keys = key.split(".")
        var currentLevel: Any? = db.data

        for (subKey in keys) {
            if (currentLevel is Map<*, *>) {
                currentLevel = currentLevel[subKey]
            } else {
                return null
            }
        }

        return currentLevel
    }

    fun delete(key: String) {
        val keys = key.split(".")
        var currentLevel = db.data

        for (i in 0 until keys.size - 1) {
            val subKey = keys[i]
            currentLevel = (currentLevel[subKey] as? MutableMap<String, Any>) ?: return
        }

        currentLevel.remove(keys.last())
        save()
    }

    fun has(key: String): Boolean {
        return get(key) != null
    }

    fun add(key: String, amount: Int) {
        val currentValue = get(key) as? Int
            ?: throw IllegalArgumentException("Value at '$key' is not a valid integer or does not exist.")
        set(key, currentValue + amount)
    }

    fun sub(key: String, amount: Int) {
        val currentValue = get(key) as? Int
            ?: throw IllegalArgumentException("Value at '$key' is not a valid integer or does not exist.")
        set(key, currentValue - amount)
    }
    
    private fun save() {
        objectMapper.writeValue(File(filePath), db)
    }
}