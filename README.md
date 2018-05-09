ChangingChromeDriverDemo
========================

# What is this? For what purpose is it?
As "https://forum.katalon.com/discussion/6150/google-chrome-crashed-on-my-pc-----2-reasons-found" records,
I have ever encountered a problem while using Katalon Studio on my corporate PC.
When I execute a test case with Google Chrome browser, chrome crashed.
After months of investigation I found the reason why Chrome crashed.
The crash occurs if one or more Force-Installed-Extension is installed in Chrome.

What is 'Force-Installed-Extension'? --- Please refer to
 https://getadmx.com/?Category=Chrome&Policy=Google.Policies.Chrome::ExtensionInstallForcelist

 Katalon Studio (or chromedriver.exe) starts the Chrome browser with the '--disable-extensions' switch.
 On the other hand, on my PC in Chrome there installed a force-installed extension which resists to be disabled.
 Due to this contradiction Chrome crashed.

 I tried to find a workaround. I have got one.
 com.kms.katalon.core.webui.driver.DriverFactory class implements changeWebDriver() method.
 Provided with changeWebDriver() method, I can instanciate a ChromeDriver myself and
 let Katalon to use it. This worked fine. Chrome does not go crashed.

 I learned aboutn DriverFactory#changeWebDriver() in the discussion of
  https://forum.katalon.com/discussion/comment/15164#Comment_15164


#

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
