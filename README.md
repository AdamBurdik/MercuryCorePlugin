# Mercury Core
**Mercury Core** is a plugin for Minecraft Paper servers.

The goal of Mercury Core is to provide an API for interacting with the server without having to use Bukkit/Paper directly.
### Mercury Core currently contains these features:
- Player translations
- Item system with blueprints and custom components
- Mob system with blueprint-based mob customizability
- Ability to create items and mobs using TOML files
- Simple placeholder system via mini-message
- Simple configuration system using TOML
- Easy way to store data in database

### Plans for the future
- Easy way to interact with packets
- Custom events
- Integration with Velocity
- Support for different types of databases (e.g., MySQL)
- Improved item and mob systems
- Integration of Lua (or another language) for simple scripts
- Documentation and wiki

## Highlights

### Interacting with database
```java
// Loads data from database and stores it in cache.
MercuryCore.loadData(playerUuid, PlayerData.class);

// Retrieves data from cache.
var data = MercuryCore.getData(playerUuid, PlayerData.class);
data.increaseScore(100);

// Saves data to database.
MercuryCore.saveData(playerUuid, PlayerData.class);

// Removes data from cache.
MercuryCore.unloadData(playerUuid, PlayerData.class);
```
- **Note:** Data holder needs to be registered for PlayerData

# How To Use
To interact with mercury core you need [MercuryAPI](https://github.com/AdamBurdik/MercuryAPI)

You can include it like this:

```kotlin
repositories {
    maven("https://jitpack.io")
}


dependencies {
    compileOnly("com.github.AdamBurdik:MercuryAPI:<commit>")
}
```

Then you need to retrieve api instance.
```java
@Override
public void onEnable() {
    Plugin corePlugin = Bukkit.getPluginManager().getPlugin("MercuryCore");
    if (corePlugin instanceof MercuryAPI api) {
        MercuryCore.setInstance(api.getMercuryCore());
    }
}
```
After setting instance to mercury core, you are able to use static methods from MercuryCore

# How To Build
1. Clone the repository
```bash
   git clone https://github.com/AdamBurdik/MercuryCorePlugin.git
```
2. Navigate to the directory
```bash
   cd MercuryCorePlugin
```
3. Build the project
```bash
   gradlew build
```