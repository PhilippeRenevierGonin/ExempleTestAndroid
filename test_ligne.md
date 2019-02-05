Quand vous clonerez cet exemple, il faudra probablement refaire le wrapper de graddle (vous aurez le message dans android studio).

Cet exemple est riche : 
 + onOptionsItemSelected de l'activité montre comment écrire dans un fichier (public)
 + MyView montre comment étendre une View pour en faire une personnalisée, avec enregistrement des évènements
 + MyViewAction (androidTest) montre comment faire sa propre ViewAction, ici pour avoir des touch
 + TestHorizontale charge des fichiers csv, listes d'évènements touch, pour reproduire des figures. Ces listes doivent être dans une certaine logique : d'abord on touche, on déplace puis on relève. Ces fichiers csv sont produit par l'application (c.f. premier point)
 + MyView est paramétrée grâce aux tests (pour déterminer une valeur de seuil) : on définit à l'avance quels tracés sont des honrizontales, puis on essaie de régler (manuellement) MyView pour que cela soit bon
 + etc.
