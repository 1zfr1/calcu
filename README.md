# Android Calculator

Basic Android calculator with:
- Addition, subtraction, multiplication, division
- Percentage
- Sign toggle
- Decimal input
- Backspace
- Clear
- Division-by-zero handling

## Build APK on GitHub from your phone

1. Create a GitHub repository.
2. Upload all files from this project (not the ZIP file itself).
3. Open the **Actions** tab.
4. Select **Build Calculator APK**.
5. Tap **Run workflow**.
6. When the workflow finishes, open the run and download the **Calculator-debug** artifact.
7. Extract the downloaded artifact and install `app-debug.apk` on your Android phone.

The workflow uses JDK 17 and the Gradle wrapper included in this project.
