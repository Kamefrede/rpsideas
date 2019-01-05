#!/usr/bin/env python3
import os

from typing import List, Optional

import cv2
import numpy as np
import colorsys

TXT_RED = "\033[91m"
TXT_YELLOW = "\033[93m"
TXT_RESET = "\033[0m"
TXT_BOLD = "\033[1m"

WHITE = [255, 255, 255]
GRAY = [91, 91, 91]
SHADE = [0, 0, 0]
BACKGROUND = [21, 21, 21]

LIGHT_EDGE = [42, 42, 42]
DARK_EDGE = [11, 11, 11]
DARK_CORNER = [5, 5, 5]

FILL_COLORS = [
    [GRAY, LIGHT_EDGE, BACKGROUND],
    [LIGHT_EDGE, BACKGROUND, DARK_EDGE],
    [BACKGROUND, DARK_EDGE, DARK_CORNER]
]

ERR = [0, 0, 255]


def is_mod_color(color: List[int]):
    hsv = colorsys.rgb_to_hsv(color[2] / 255.0, color[1] / 255.0, color[0] / 255.0)
    return 0.15 <= hsv[1] <= 0.35 and hsv[2] == 1


def target_color(x: int, y: int) -> Optional[List[int]]:
    if x != 0 and y != 0:
        return

    x_index = 0 if x == 0 else (2 if x == 15 else 1)
    y_index = 0 if y == 0 else (2 if y == 15 else 1)

    return FILL_COLORS[y_index][x_index]


def find_warnings(texture: np.ndarray) -> List[str]:
    out = []

    for x in range(16):
        for y in range(16):
            color_at = texture[y, x]

            target = target_color(x, y)
            if target is not None and any(color_at != target):
                out.append("The edge wasn't the expected color at ({0}, {1})".format(str(x), str(y)))
                img[y, x] = target

            within = 2 <= x <= 13 and 2 <= y <= 13
            if within and (all(color_at == WHITE) or is_mod_color(color_at)):
                shadow_color = texture[y + 1, x + 1]
                if any(shadow_color != WHITE) and \
                        any(shadow_color != GRAY) and \
                        any(shadow_color != SHADE) and \
                        not is_mod_color(shadow_color):
                    out.append("The shadows weren't populated as expected at ({0}, {1})".format(str(x), str(y)))
                    img[y + 1, x + 1] = SHADE

            within = 1 <= x <= 14 and 1 <= y <= 14
            if within and all(color_at == SHADE):
                shader_color = texture[y - 1, x - 1]
                if any(shader_color != WHITE) and not is_mod_color(shader_color):
                    out.append("Stray shadow at ({0}, {1})".format(str(x), str(y)))
                    img[y, x] = BACKGROUND

    return out


def find_soft_warnings(texture: np.ndarray) -> List[str]:
    out = []

    for x in range(16):
        for y in range(16):
            color_at = texture[y, x]

            within = x == 1 or x == 14 or y == 1 or y == 14
            if within and (all(color_at == WHITE) or is_mod_color(color_at)):
                out.append("The boundaries had color at ({0}, {1})".format(str(x), str(y)))

    return out


def find_errors(texture: np.ndarray) -> List[str]:
    out = []

    for x in range(16):
        for y in range(16):
            color_at = texture[y, x]
            within = 1 <= x <= 14 and 1 <= y <= 14
            if within and \
                    any(color_at != WHITE) and \
                    any(color_at != GRAY) and \
                    any(color_at != SHADE) and \
                    any(color_at != BACKGROUND) and \
                    not is_mod_color(color_at):
                if all(color_at == [98, 98, 98]):
                    img[y, x] = GRAY
                else:
                    out.append("There was an invalid color {0} at ({1}, {2})".format(str(color_at), str(x), str(y)))
                    img[y, x] = ERR

    return out


if __name__ == "__main__":
    files = [f for f in os.listdir(os.getcwd()) if f.endswith(".png")]
    for fname in files:
        img = cv2.imread(fname)
        if img.shape[0] != 16 or img.shape[1] != 16:
            print(TXT_BOLD + "Searching: " + TXT_RESET + fname)
            print(TXT_RED + TXT_BOLD + "Error: " + TXT_RESET + "Invalid dimensions")
            print()
        else:
            if img.shape[2] == 4:
                img = cv2.cvtColor(img, cv2.COLOR_RGBA2RGB)
            errors = find_errors(img)
            soft_warnings = find_soft_warnings(img)
            warnings = find_warnings(img)

            if errors or warnings or soft_warnings:
                print(TXT_BOLD + "Searching: " + TXT_RESET + fname)

            for error in errors:
                print(TXT_RED + TXT_BOLD + "Error: " + TXT_RESET + error)
            for warning in warnings:
                print(TXT_YELLOW + TXT_BOLD + "Warning: " + TXT_RESET + warning)
            for warning in soft_warnings:
                print(TXT_YELLOW + "Info: " + TXT_RESET + warning)

            if errors or warnings:
                print(TXT_BOLD + "Writing: " + TXT_RESET + fname)
                cv2.imwrite(fname, img)

            if errors or warnings or soft_warnings:
                print()



