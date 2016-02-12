Debug helpers for apps
=========================

![Image](img/android.gif)

# Setup in project 

Include it in your `gradle.properties`:

* Release:
```
repositories {
  maven {
      url 'http://archiva.barista-v.com:8080/repository/internal/'
      credentials {
        username "${NEXUS_USERNAME}"
        password "${NEXUS_PASSWORD}"
      }
  }
}

dependencies {
  devCompile 'com.barista_v:debugging:0.1.2'
}
```

* Snapshots:
```
repositories {
  maven {
      url 'http://archiva.barista-v.com:8080/repository/snapshots/'
      credentials {
        username "${NEXUS_USERNAME}"
        password "${NEXUS_PASSWORD}"
      }
  }
}

dependencies {
  devCompile 'com.barista_v:debugging:0.0.1-SNAPSHOT'
}
```

# Use

Check sample module to see how it can be used.

- Debug Drawer: add a debug drawer with info about the app and some debugging tools.
![Image2](img/debug_drawer.png)
- ViewServer: helps to debug layouts

# Publish

1. Check `gradle.properties` and `debugging/gradle.properties` for POM setup.
1. Run: `scripts/publish.sh`

# Using
https://raw.github.com/danielgomezrico/gradle-mvn-push/master/gradle-mvn-push.gradle
