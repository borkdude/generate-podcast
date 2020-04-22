# Generate podcast

A [babashka](https://github.com/borkdude/babashka/) script to transform a
directory of mp3 files into an XML file that can be used to subscribe as a
podcast in iTunes (and possibly other music programs?).

This is a great way to enjoy conference talks on your iPod or mobile phone.

<img src="nano.jpg" width="50%">

## Usage

- Install [babashka](https://github.com/borkdude/babashka/).
- Place the script `generate_podcast.clj` somewhere on your path.

- Create a directory with `.mp3` files. E.g. download [Clojure Conj
2019](https://www.youtube.com/watch?v=MnvtPzEH-d8&list=PLZdCLR02grLqSy15ALLAZDU6LGpAJDrAQ)
as mp3 files with [youtube-dl](https://github.com/ytdl-org/youtube-dl).

  ``` shell
  $ mkdir -p conj2019 && cd conj2019
  $ youtube-dl -x --audio-format mp3 "https://www.youtube.com/watch?v=MnvtPzEH-d8&list=PLZdCLR02grLqSy15ALLAZDU6LGpAJDrAQ"
  ```

- Start a local webserver in the mp3-files directory

   ``` shell
   $ python -m SimpleHTTPServer
   Serving HTTP on 0.0.0.0 port 8000 ...
   ```

- In another shell, generate the podcast file from inside the mp3-files directory:

   ``` shell
   $ cd conj2019
   $ generate_podcast.clj -h
   generate_podcast.clj:

   -f --file FILE Podcast file to generate :default podcast.xml
   -t --title TITLE Title for the podcast
   -b --base-url URL Base URL of server
   -h --help

   $ generate_podcast.clj -f conj2019.xml -b http://localhost:8000 -t "Clojure Conj 2019"
   Written podcast for Clojure Conj 2019 at conj2019.xml
   ```

- In iTunes, subscribe to the podcast: `File > Subscribe to podcast > http://localhost:8000/conj2019.xml`.

Happy listening!

## License

Copyright © 2020 Michiel Borkent

Distributed under the EPL License. See LICENSE.
