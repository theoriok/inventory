import {cleanup} from '@testing-library/react';
import {afterEach, beforeAll, vi} from 'vitest';

import '@testing-library/jest-dom/vitest';


// runs a clean after each test case (e.g. clearing jsdom)
afterEach(() => {
    cleanup();
});

beforeAll(() => {
    const { getComputedStyle } = window;
    window.getComputedStyle = (elt) => getComputedStyle(elt);
    Element.prototype.scrollIntoView = vi.fn(); // see https://github.com/jsdom/jsdom/issues/1695#issuecomment-449931788

    // Mock ResizeObserver
    globalThis.ResizeObserver = class ResizeObserver {
        observe() {}
        unobserve() {}
        disconnect() {}
    };

    // Mock transitionend events for jsdom v28+
    // jsdom v28 added TransitionEvent but doesn't fire transitionend events
    const originalAddEventListener = Element.prototype.addEventListener;
    Element.prototype.addEventListener = function(type: string, listener: EventListenerOrEventListenerObject, options?: boolean | AddEventListenerOptions) {
        originalAddEventListener.call(this, type, listener, options);
        if (type === 'transitionend') {
            setTimeout(() => {
                const event = new TransitionEvent('transitionend', { bubbles: true });
                this.dispatchEvent(event);
            }, 0);
        }
    };
});

Object.defineProperty(window, 'matchMedia', {
    writable: true,
    value: vi.fn().mockImplementation((query) => ({
        matches: false,
        media: query,
        onchange: null,
        addListener: vi.fn(), // deprecated
        removeListener: vi.fn(), // deprecated
        addEventListener: vi.fn(),
        removeEventListener: vi.fn(),
        dispatchEvent: vi.fn(),
    })),
});
