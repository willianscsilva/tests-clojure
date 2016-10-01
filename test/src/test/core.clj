(ns test.core
    (require [clojure.java.jdbc :as j])
    (import java.util.concurrent.Executors))

(defn test-stm [nitems nthreads niters]
  (let [refs  (map ref (repeat nitems 0))
        pool  (Executors/newFixedThreadPool nthreads)
        tasks (map (fn [t]
                      (fn []
                        (println  niters)
                        (dotimes [n niters]
                          (dosync
                            (doseq [r refs]
                              (alter r + 1 t))))))
                   (range nthreads))]
    (doseq [future (.invokeAll pool tasks)]
      (.get future))
    (.shutdown pool)
    (map deref refs)))

(def mysql-db {:classname "com.mysql.jdbc.Driver"
               :dbtype "mysql"
               :dbname "teste"
               :user "root"
               :password "pass"})

(defn select-product []
    (j/query mysql-db
        ["select * from produtos where id = ?" "1"]
        {:row-fn :nome}))

(defn return-str [] "Some string!")

(defn read-args [args iter]
    (println (nth args iter))
    (when (< iter (- (count args) 1))
        (read-args args (+ iter 1))))

(defn -main [& args]
   (println "Hello, World!")
   (println
       (str "Args: " (nth args 0)))
   (read-args args 0)
   (println
       (return-str))
   (println (select-product))
   (test-stm 10 10 10000))
