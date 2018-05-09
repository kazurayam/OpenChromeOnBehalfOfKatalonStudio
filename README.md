ChangingChromeDriverDemo
========================

# What is this?

This is a simple [Katalon Studio](https://www.katalon.com/) project for demonstration purpose.
You can check this out onto your PC and execute with you Katalon Stduio.



This project presents a solution to a problem I raised at a post in the Katalon Forum [google chrome crashed on my pc ---- 2 reasons found]( https://forum.katalon.com/discussion/6150/google-chrome-crashed-on-my-pc-----2-reasons-found)

# How to run the example

Once cloned the project on your PC, start Katalon Studio and open the project.

This project is developed with Katalon Studio ver5.4.1.

Check the Execution Profile where you find 2 global variables: KATALONSTUDIO_HOME, DRIVERLOG_OUTPUT_DIRECTORY. Please change the value to fit your environment.

Select one of test cases and run it.

| Test Case name | What it does | expected result |
|:---------------|:-------------|:----:|
| TC1_defaultWayOfOpeningBrowser | calls `WebUI.openBrowser()` as usual | should succeed |
| TC2_openOrdinaryChrome         | instanciates ChromeDriver without additional options, and let Katalon Studio to use it | should succeed |
| TC3_openChromeWithSwitches     | instanciates ChromeDriver with an additional option, and let Katalon Studio to use it. | should fail |

# Description
## My problem to solve

As my post [google chrome crashed on my pc ---- 2 reasons found]( https://forum.katalon.com/discussion/6150/google-chrome-crashed-on-my-pc-----2-reasons-found) describes, Katalon Studio on my PC always fails to open Google Chrome browser. I have found out why.

1. Katalon Studio wants to start chrome.exe with `--disable-extensions` switch. This is proved by looking at the chromedriver.log file.
2. On the other hand, on my PC, there is a [Force-Installed-Extension](https://getadmx.com/?Category=Chrome&Policy=Google.Policies.Chrome::ExtensionInstallForcelist) in the Chrome browser. A Force-Installed-Extension can not be disabled.

This contradiction brings the Google Chrome crazy; it crashes.

What is 'Force-Installed-Extension'? --- Please refer to
 https://getadmx.com/?Category=Chrome&Policy=Google.Policies.Chrome::ExtensionInstallForcelist

## Way of working-around

By looking at the log file of chromdriver.exe, I realized that the Katalon Studio wants chromedriver to generate a command to start chrome.exe with `--disable-extensions` switch. This switch was the cause of the crash. So I want to start chrome without `--disable-exteions` switch. I studied many posts in the Katalon Forum and have got an idea:

1. I will not rely on Katalon Studio to open browser.
1. Rather my test case will instanciate  `org.selenium.org.openqa.selenium.chrome.ChromeDriver`.
1. My test case lets Katalon Studio to use the ChromeDriver instance for running tests. This can be done by using  `com.kms.katalon.core.webui.driver.DriverFactory#changeWebDriver()` method.

I learned about DriverFactory#changeWebDriver() in the discussion at https://forum.katalon.com/discussion/comment/15164#Comment_15164


## Design detail

```
"C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" --chrome.switches --disable-background-networking --disable-client-side-phishing-detection --disable-default-apps --disable-extensions --disable-extensions-except="C:\Users\username\AppData\Local\Temp\scoped_dir9548_2256\internal" --disable-hang-monitor --disable-popup-blocking --disable-prompt-on-repost --disable-sync --disable-web-resources --enable-automation --enable-logging --force-fieldtrials=SiteIsolationExtensions/Control --ignore-certificate-errors --log-level=0 --metrics-recording-only --no-first-run --password-store=basic --proxy-server=ftp=172.24.2.10:8080;http=172.24.2.10:8080;https=172.24.2.10:8080 --remote-debugging-port=12705 --test-type=webdriver --use-mock-keychain --user-data-dir="C:\Users\username\AppData\Local\Temp\scoped_dir9548_25002" data:, |
```

```
"C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" --disable-background-networking --disable-client-side-phishing-detection --disable-default-apps --disable-hang-monitor --disable-popup-blocking --disable-prompt-on-repost --disable-sync --disable-web-resources --enable-automation --enable-logging --force-fieldtrials=SiteIsolationExtensions/Control --ignore-certificate-errors --load-extension="C:\Users\username\AppData\Local\Temp\scoped_dir8660_29138\internal" --log-level=0 --metrics-recording-only --no-first-run --password-store=basic --remote-debugging-port=12555 --test-type=webdriver --use-mock-keychain --user-data-dir="C:\Users\username\AppData\Local\Temp\scoped_dir8660_21653" data:,
```

Let me check every arguments:

| Default | Change |
|:----|:----|
| "C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" | "C:\Program Files (x86)\Google\Chrome\Application\chrome.exe" |
| `--chrome.switches`             | |
| --disable-background-networking | --disable-background-networking |
| --disable-client-side-phishing-detection | --disable-client-side-phishing-detection |
| --disable-default-apps          | --disable-default-apps |
| `--disable-extensions`          | |
| --disable-extensions-except="C:\Users\username\AppData\Local\Temp\scoped_dir9548_2256\internal" | |
| --disable-hang-monitor          | --disable-hang-monitor |
| --disable-popup-blocking        | --disable-popup-blocking |
| --disable-prompt-on-repost      | --disable-prompt-on-report |
| --disable-sync                  | --disable-sync |
| --disable-web-resources         | --disable-web-resources |
| --enable-logging                | --enable-logging |
| --force-fieldtrials=SiteIsolationExtensions/Control | --force-fieldtrials=SiteIsolationExtensions/Control |
| --ignore-certificate-errors     | --ignore-certificate-errors |
| --log-level=0                   | --log-level=0 |
| --metrics-recording-only        | --metrics-recording-only |
| --no-first-run                  | --no-first-run |
| --password-store=basic          | --password-store=basic |
| --proxy-server=ftp=172.24.2.10:8080;http=172.24.2.10:8080;https=172.24.2.10:8080 | |
| --remote-debugging-port=12705   | --remote-debugging-port=12555 |
| --test-type=webdriver           | --test-type=webdriver |
| --use-mock-keychain             | --use-mock-keychain |
| --user-data-dir="C:\Users\username\AppData\Local\Temp\scoped_dir9548_25002" | --user-data-dir="C:\Users\username\AppData\Local\Temp\scoped_dir8660_21653" |
| data:,                          | data:, |


## Let me analyze the difference between the two

[List of Chromium Command Line Switches](https://peter.sh/experiments/chromium-command-line-switches/) ... *you need to wait over 10 seconds for response*

| switch, which has difference | note |
|:----|:----|
| `--chrome.switches` | This is not listed in the above blog page. It is likely that this switch is just ignored by Chrome; might be a mistake of Katalon Studio. |
| `--disable-extensions` | *Disable extensions.*  |
| `--disable-extensions-except=...` | *Disable extensions except those specified in a comma-separated list.* |
| `--proxy-server=...;http=172.24.2.10:8080;...` | *Uses a specified proxy server, overrides system settings. This switch only affects HTTP and HTTPS requests.* |
| `--remote-debugging-port=12705` | *Enables remote debug over HTTP on the specified port.   |
| `--user-data-dir=...`   | *Directory where the browser stores the user profile.* |
