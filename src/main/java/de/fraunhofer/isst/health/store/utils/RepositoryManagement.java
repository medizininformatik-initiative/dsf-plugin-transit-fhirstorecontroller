package de.fraunhofer.isst.health.store.utils;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class RepositoryManagement {
    private static final Logger LOGGER = Logger.getLogger(RepositoryManagement.class.getName());
    private final String url;
    private final String branch;
    private final String dupIdentifier;
    private String username;
    private String password;
    private boolean withAuthorization;
    private Git git;
    private File repoPath;
    public RepositoryManagement(String dupId, String url, String branch, String username, String password) {
        this(dupId, url, branch);
        this.username = username;
        this.password = password;
        this.withAuthorization = true;
    }
    public RepositoryManagement(String dupId, String url, String branch) {
        this.url = url;
        this.branch = branch;
        this.dupIdentifier = dupId;
        this.withAuthorization = false;
    }

    public void cloneInto(String path) throws GitAPIException, IOException {

        repoPath = File.createTempFile(path, "");
        if (!repoPath.delete()) {
            throw new IOException("Could not delete temporary file " + repoPath);
        }
        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(url)
                .setBranch(branch);
        if (withAuthorization) {
            cloneCommand.setCredentialsProvider(credentialsProvider());
        }
        LOGGER.info("Cloning repository from " + url);
        git = cloneCommand.setDirectory(repoPath).call();
    }

    public File readFile(String prefix, String suffix){
        String path = git.getRepository().getDirectory().getParent() + "/" + prefix;
        return new File(
                path,
                suffix);
    }

    public void writeFile(String prefix, String suffix, String content, boolean append) throws IOException {
        String path = git.getRepository().getDirectory().getParent() + "/" + prefix;
        LOGGER.info("Writing file " + suffix + " to path " + path + " with append = " + append);
        File file = new File(
                path,
                suffix);
        if (append && !FileUtils.readFileToString(file, StandardCharsets.UTF_8).endsWith("\n")) {
            content = "\n" + content;
        }
        FileUtils.write(file, content, StandardCharsets.UTF_8, append);
    }
    public void add(String pattern) throws GitAPIException {
        LOGGER.info("Adding Changes with pattern " + pattern + " to staging");
        git.add().addFilepattern(pattern).call();
    }
    public void addAll() throws GitAPIException {
        add(".");
    }
    public void commit(String message) throws GitAPIException {
        LOGGER.info("Committing changes for project " + dupIdentifier);
        git
                .commit()
                .setAuthor("author", "122803604+nhaldorn@users.noreply.github.com")
                .setCommitter("archive-controller", "122803604+nhaldorn@users.noreply.github.com")
                .setMessage(message)
                .call();
    }
    public void push() throws GitAPIException {
        LOGGER.info("Pushing changes for project " + dupIdentifier);
        PushCommand command = git.push();
        if (withAuthorization) {
            command.setCredentialsProvider(credentialsProvider());
        }
        command.call();
        LOGGER.info("Changes pushed");
    }
    public void close() throws IOException {
        git.close();
        FileUtils.deleteDirectory(repoPath);
    }

    public Git getGit() {
        return git;
    }
    private UsernamePasswordCredentialsProvider credentialsProvider() {
        return new UsernamePasswordCredentialsProvider(
                username,
                password);
    }

}
