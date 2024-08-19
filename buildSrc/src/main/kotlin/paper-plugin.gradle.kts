import com.ryderbelserion.feather.enums.Repository

plugins {
    id("java-plugin")
}

repositories {
    maven(Repository.Paper.url)
}