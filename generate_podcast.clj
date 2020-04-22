#!/usr/bin/env bb

(ns generate-podcast
  (:require [clojure.java.io :as io]
            [clojure.data.xml :as xml]
            [clojure.string :as str]
            [clojure.tools.cli :as cli])
  (:import [java.io File]
           [java.nio.file Files LinkOption]))

(defn created [^File f]
  (-> (.toPath f)
      (Files/getAttribute  "creationTime" (into-array LinkOption []))
      str))

(defn item [^File f base-url]
  (xml/element
   :item
   {}
   (xml/element :title {} (.getName f))
   (xml/element :pubDate {} (created f))
   (xml/element :enclosure {:url (str base-url "/"
                                      (-> (.getName f)
                                          (java.net.URLEncoder/encode)
                                          (str/replace "+" "%20")))
                            :type "audio/mpeg"})))

(defn rss [title base-url podcast-file]
  (xml/element
   :rss {}
   (xml/element
    :channel {:version "2.0"
              :xmlns/itunes "http://www.itunes.com/dtds/podcast-1.0.dtd"}
    (xml/element :title {} title)
    (xml/element :link {} (str base-url "/" podcast-file))
    (for [^File f (file-seq (io/file "."))
          :when (str/ends-with? (.getName f) ".mp3")]
      (item f base-url)))))

(def cli-options
  [["-f" "--file FILE" "Podcast file to generate"
    :default "podcast.xml"]
   ["-t" "--title TITLE" "Title for the podcast"]
   ["-b" "--base-url URL" "Base URL of server"]
   ["-h" "--help"]])

(defn print-help
  []
  (println "podcast.clj:")
  (println)
  (doseq [o cli-options]
    (apply println o)))

(let [args (cli/parse-opts *command-line-args* cli-options)
      {:keys [:file :base-url :title :help]} (:options args)]
  (if (or (not file)
          (not base-url)
          (not title)
          help)
    (print-help)
    (let [base-url (str/replace base-url #"/*$" "")]
      (spit file
            (xml/emit-str
             (rss title base-url file)))
      (println "Written podcast for" title "at" file))))
