import { Capacitor } from '@capacitor/core';

const WEB_API_BASE_URL = 'http://localhost:8080';
const ANDROID_EMULATOR_API_BASE_URL = 'http://10.0.2.2:8080';

export const API_BASE_URL = Capacitor.isNativePlatform()
  ? ANDROID_EMULATOR_API_BASE_URL
  : WEB_API_BASE_URL;
