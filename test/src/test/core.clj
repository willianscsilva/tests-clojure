(ns test.core
    (require [clojure.java.jdbc :as j]))

(def mysql-db {:classname "com.mysql.jdbc.Driver"
               :dbtype "mysql"
               :dbname "teste"
               :user "root"
               :password "sac;201!"})
(defn select-product []
    (j/query mysql-db
        ["select * from produtos where id = ?" "1"]
        {:row-fn :nome}))

(defn return-str [] "Some string!")

(defn read-args [args iter]
    (println (nth args iter))
    (if (< iter (- (count args) 1))
        (read-args args (+ iter 1))
         nil))

(defn -main [& args]
   (println "Hello, World!")
   (println
       (str "Args: " (nth args 0)))
   (read-args args 0)
   (println
       (return-str))
   (println (select-product)))
