package org.daniel.github.test;

import org.openqa.selenium.By;

public class Selectors {
    public static final By SIGN_IN_LINK = By.linkText("Sign in");
    public static final By USERNAME = By.id("login_field");
    public static final By PASSWORD = By.id("password");
    public static final By SIGN_IN_BUTTON = By.name("commit");


    public static final By CREATE_NEW_BUTTON = By.className("octicon-plus");
    public static final By CREATE_NEW_REPOSITORY_BUTTON = By.linkText("New repository");

    public static final By REPOSITORY_NAME_INPUT = By.id("repository_name");
    public static final By CREATE_REPOSITORY_BUTTON = By.className("btn-primary");
    public static final By INIT_REPO_CHECKBOX = By.id("repository_auto_init");

    public static final By COMMITS_TAB = By.cssSelector(".commits>a");

    public static final By CREATE_PULL_REQUEST_RADIO_OPTION = By.cssSelector("input[value='quick-pull']");
    public static final By CREATE_PULL_REQUEST_NAME_INPUT = By.cssSelector("svg.quick-pull-new-branch-icon + input");
    public static final By SUBMIT_NEW_FILE_BUTTON = By.id("submit-file");
    public static final By CREATE_PULL_REQUEST_BUTTON = By.cssSelector(".form-actions > button");
    public static final By MERGE_WINDOW = By.className("mergeability-details");

    public static final By PULL_REQUESTS_TAB = By.cssSelector(".octicon-git-pull-request + span");
    public static final By PULL_REQUEST_ENTRY = By.cssSelector(".js-issue-row .js-navigation-open");
    public static final By ACCEPT_PULL_REQUEST_BUTTON = By.cssSelector("div.btn-group-merge > button[type='submit']");
    public static final By CONFIRM_ACCEPT_PULL_REQUEST_BUTTON = By.className("js-merge-commit-button");
    public static final By MERGED_PURPLE_ICON = By.className("State--purple");

    public static final By SETTINGS_TAB = By.className("octicon-gear");
    public static final By REMOVE_REPO_BUTTON = By.cssSelector("button[data-facebox='#delete_repo_confirm']");
    public static final By DELETE_REPO_NAME_INPUT = By.cssSelector(".facebox-content form p > input");
    public static final By DELETE_REPO_CONFIRM_BUTTON = By.cssSelector(".facebox-content button[type='submit']");

    public static final By REPO_LINK = By.linkText(GithubTest.TEST_REPO_NAME);



}
