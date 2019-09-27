(ns breaking-bad-quotes.core
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET]]))


(defn fetch-link! [data]
  (GET "https://breaking-bad-quotes.herokuapp.com/v1/quotes"
       {:handler #(reset! data %)
        :error-handler (fn [{:keys [status status-text]}]
                         (js/console.log status status-text))}))
     

(defn quote []
  (let [data (atom "yoyo!")]
    (fetch-link! data)
    (fn []
      (let [{:strs [quote author]} (first @data)]
      [:div.cards>div.card
        [:div.card-body.text-center
         [:p#quote quote]
         [:p#author author]]
        [:div.card-footer.center.text-center
         [:button#twitter.outline>a#tweet
          {:href "#"
           :target "_blank"
           }
          [:i.fi-social-twitter " Tweet"]]
         [:button#new-quote.outline
          {:on-click #(fetch-link! data)}
          [:i.fi-shuffle " New Quote"]]]]))))

(defn start []
  (reagent/render-component [quote]
                            (. js/document (getElementById "app"))))

(defn ^:export init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (start))

(defn stop []
  ;; stop is called before any code is reloaded
  ;; this is controlled by :before-load in the config
  (js/console.log "stop"))
