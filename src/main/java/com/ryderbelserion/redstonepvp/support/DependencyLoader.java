package com.ryderbelserion.redstonepvp.support;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class DependencyLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        final MavenLibraryResolver resolver = new MavenLibraryResolver();

        //resolver.addDependency(new Dependency(new DefaultArtifact("dev.jorel:commandapi-bukkit-core:9.5.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("com.ryderbelserion.vital:paper:2.0.1"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("com.zaxxer:HikariCP:4.0.3"), null));

        resolver.addRepository(new RemoteRepository.Builder("codemc", "default", "https://repo.codemc.org/repository/maven-public/").build());
        resolver.addRepository(new RemoteRepository.Builder("paper", "default", "https://repo.papermc.io/repository/maven-public/").build());
        resolver.addRepository(new RemoteRepository.Builder("crazycrewReleases", "default", "https://repo.crazycrew.us/releases/").build());

        classpathBuilder.addLibrary(resolver);
    }
}