export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        forest: "#14281D",
        pine: "#1B4332",
        leaf: "#2D6A4F",
        moss: "#40916C",
        sage: "#74C69D",
        mint: "#B7E4C7",
        foam: "#D8F3DC",
        cream: "#F6F1E7",
        sand: "#E9EDC9",
        clay: "#C9A227",
        bark: "#3D2B1F",
      },
      fontFamily: {
        display: ["Fraunces", "Georgia", "serif"],
        sans: ["Outfit", "system-ui", "sans-serif"],
      },
      boxShadow: {
        soft: "0 20px 50px -24px rgba(27, 67, 50, 0.45)",
      },
    },
  },
  plugins: [],
};
