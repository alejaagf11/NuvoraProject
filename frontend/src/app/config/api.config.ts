  const WEB_API_BASE_URL = 'https://nuvoraproject-production.up.railway.app';

  const ANDROID_EMULATOR_API_BASE_URL = 'http://10.0.2.2:8080';

  const isNative = typeof window !== 'undefined' && window.hasOwnProperty('Capacitor');

  export const API_BASE_URL = isNative
    ? ANDROID_EMULATOR_API_BASE_URL
    : WEB_API_BASE_URL;