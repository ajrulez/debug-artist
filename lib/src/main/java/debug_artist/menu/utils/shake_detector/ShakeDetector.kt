package debug_artist.menu.utils.shake_detector

interface ShakeDetector {
  fun start(listener: OnShakeListener)
  fun pause()
}

interface OnShakeListener {

  /**
   * @param count number of shakes that were made one after another.
   */
  fun onShake(count: Int)
}