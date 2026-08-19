# Keep names while allowing R8 shrinking and optimization.
-dontobfuscate

# Gson models
-keep class tech.baza_trainee.mama_ne_vdoma.data.model.** { *; }
-keep class tech.baza_trainee.mama_ne_vdoma.data.utils.CustomResponse { *; }

# Gson reflection metadata
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.Unsafe

# Retrofit annotations
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations
-keepclassmembernames interface * {
    @retrofit2.http.* <methods>;
}

-keep,allowoptimization class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}

# Kotlin Serialization navigation routes
-keep class tech.baza_trainee.mama_ne_vdoma.presentation.navigation.routes.** { *; }

# Stack traces
-keepattributes SourceFile,LineNumberTable