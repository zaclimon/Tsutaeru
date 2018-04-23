# Tsutaeru

Tsutaeru simplifies IPTV watching experience for Xtream Codes based providers only on Android TV. All of this in one's home sweet home.

## Getting started for development

- `git clone https://github.com/zaclimon/Tsutaeru`
- Open in Android Studio
- Enjoy!

### Getting xipl working

You might want to get [xipl](https://github.com/zaclimon/xipl) running alongside Tsutaeru for developping new features. (Live TV, UI or Playback) If that's the case, do the following:

1. Clone xipl

    `git clone https://github.com/zaclimon/xipl`
2. Modify `settings.gradle` to configure xipl and tiflibrary
    ```groovy
    include ':app', ':xipl', ':tiflibrary'
    project (':xipl').projectDir = new File('path/to/xipl')
    project (':tiflibrary').projectDir = new File('path/to/tiflibrary')
    ```
3. In the app's `build.gradle`
    ```groovy
    // Remove or comment this line
    implementation 'com.zaclimon:xipl:x.y.z'
    // Add this line
    implementation project(':xipl')
    ```

## License

    Copyright (C) 2018 Isaac Pateau

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.