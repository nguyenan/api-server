package com.wut.datasources.egit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.eclipse.egit.github.core.Blob;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.CommitUser;
import org.eclipse.egit.github.core.Reference;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.TreeEntry;
import org.eclipse.egit.github.core.TypedResource;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.DataService;
import org.eclipse.egit.github.core.service.RepositoryService;

import com.wut.support.binary.Base64;
import com.wut.support.settings.SettingsManager;

public class EGitObject {

	static GitHubClient client;
	static ContentsService contentService;
	static RepositoryService service;
	static String branch = "feature/TENDOFFICE-3089";
	static {
		client = new GitHubClient().setOAuth2Token(SettingsManager.getSystemSetting("github.token"));
		contentService = new ContentsService(client);
		service = new RepositoryService(client);
	}

	public static String pullFile(String file, String owner, String repositoryName, String tmpConfigFile)
			throws IOException {
		byte[] fileContents = readFileContents(file, owner, repositoryName);
		return saveToFile(fileContents, tmpConfigFile);

	}

	public static byte[] readFileContents(String file, String owner, String repositoryName) throws IOException {
		Repository repository = service.getRepository(owner, repositoryName);
		List<RepositoryContents> contents = contentService.getContents(repository, file, branch);
		return (Base64.decodeBase64(contents.get(0).getContent()));
	}

	public static String pushFile(String owner, String repositoryName, String path, String localFile, String commitMessage) {
		try {
			// create needed services
			RepositoryService repositoryService = new RepositoryService();
			CommitService commitService = new CommitService(client);
			DataService dataService = new DataService(client);

			// get some sha's from current state in git
			Repository repository = repositoryService.getRepository(owner, repositoryName);
			String baseCommitSha = repositoryService.getBranches(repository).get(0).getCommit().getSha();
			RepositoryCommit baseCommit = commitService.getCommit(repository, baseCommitSha);
			String treeSha = baseCommit.getSha();

			// create new blob with data
			Blob blob = new Blob();
			byte[] encoded = Files.readAllBytes(Paths.get(localFile));
			String content = new String(encoded, "UTF-8");
//			String content = new Scanner(new File(localFile)).useDelimiter("\\Z").next();
			blob.setContent(content).setEncoding(Blob.ENCODING_UTF8);
			String blob_sha = dataService.createBlob(repository, blob);
			Tree baseTree = dataService.getTree(repository, treeSha);

			// create new tree entry
			TreeEntry treeEntry = new TreeEntry();

			// working
			treeEntry.setPath(path);
			treeEntry.setMode(TreeEntry.MODE_BLOB);
			treeEntry.setType(TreeEntry.TYPE_BLOB);
			treeEntry.setSha(blob_sha);
			treeEntry.setSize(blob.getContent().length());
			Collection<TreeEntry> entries = new ArrayList<TreeEntry>();
			entries.add(treeEntry);
			Tree newTree = dataService.createTree(repository, entries, baseTree.getSha());

			// create commit
			Commit commit = new Commit();
			commit.setMessage(commitMessage);
			commit.setTree(newTree);

			CommitUser author = new CommitUser();
			author.setName("annguyen-qh");
			author.setEmail("annguyen.qh@gmail.com");
			Calendar now = Calendar.getInstance();
			author.setDate(now.getTime());
			commit.setAuthor(author);
			commit.setCommitter(author);

			List<Commit> listOfCommits = new ArrayList<Commit>();
			listOfCommits.add(new Commit().setSha(baseCommitSha));
			commit.setParents(listOfCommits);
			Commit newCommit = dataService.createCommit(repository, commit);

			// create resource
			TypedResource commitResource = new TypedResource();
			commitResource.setSha(newCommit.getSha());
			commitResource.setType(TypedResource.TYPE_COMMIT);
			commitResource.setUrl(newCommit.getUrl());

			// get master reference and update it
			Reference reference = dataService.getReference(repository, "refs/heads/" + branch);
			reference.setObject(commitResource);
			dataService.editReference(repository, reference, true);

			// success

		} catch (Exception e) {
			// error
			e.printStackTrace();
			return "error";
		}
		return "OK";
	}
	// public static BufferedReader readFileAsBf(String file, String owner,
	// String repositoryName) throws IOException {
	// Repository repository = service.getRepository(owner, repositoryName);
	// List<RepositoryContents> contents =
	// contentService.getContents(repository, file, branch);
	// byte[] content = (Base64.decodeBase64(contents.get(0).getContent()));
	// InputStream is = null;
	// BufferedReader buf = null;
	// try {
	// is = new ByteArrayInputStream(content);
	// buf = new BufferedReader(new InputStreamReader(is));
	// } finally {
	// try {
	// if (is != null)
	// is.close();
	// } catch (Exception ex) {
	//
	// }
	// }
	// return buf;
	// }

	public static String saveToFile(byte[] content, String path) throws IOException {
		FileOutputStream fos = null;
		File f = new File(path);
		if (!f.exists()) {
			f.getParentFile().mkdirs();
			f.createNewFile();
		}
		fos = new FileOutputStream(path);
		fos.write(content);
		fos.close();
		return path;
	}
}